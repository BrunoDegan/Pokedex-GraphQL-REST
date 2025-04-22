package com.brunodegan.pokedex.ui.screen.details.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunodegan.pokedex.base.errors.ErrorData
import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
import com.brunodegan.pokedex.domain.getPokemonDetailsById.GetPokemonDetailsByIdUseCase
import com.brunodegan.pokedex.ui.mappers.PokemonDetailsViewDataMapper
import com.brunodegan.pokedex.ui.screen.details.events.PokemonDetailsUiEvents
import com.brunodegan.pokedex.ui.screen.details.state.PokemonDetailsUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PokemonViewModelDetails(
    private val mapper: PokemonDetailsViewDataMapper,
    private val useCase: GetPokemonDetailsByIdUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _snackbarState = Channel<SnackbarUiStateHolder>()
    val snackbarState = _snackbarState.receiveAsFlow()

    private val _uiState = MutableStateFlow<PokemonDetailsUiState>(PokemonDetailsUiState.Initial)
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PokemonDetailsUiState.Initial,
    )

    fun getPokemonDetails(id: Int? = null, errorMessage: String) {
        viewModelScope.launch {

            // Fail fast principle
            if (id == null) {
                _uiState.update {
                    PokemonDetailsUiState.Error(
                        ErrorData(
                            msg = errorMessage,
                        )
                    )
                }
                _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(errorMessage))
                return@launch
            }

            runCatching {
                useCase(id = id)
                    .flowOn(dispatcher)
                    .onStart { _uiState.update { PokemonDetailsUiState.Loading } }
                    .catch {
                        _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(errorMessage))
                    }.collectLatest { responseData ->
                        when (responseData) {
                            is NetworkResponse.Success -> {
                                _uiState.update {
                                    PokemonDetailsUiState.Success(mapper.map(responseData.data))
                                }
                            }

                            is NetworkResponse.Error -> {
                                val errorMsg = responseData.error?.message ?: errorMessage
                                _uiState.update {
                                    PokemonDetailsUiState.Error(
                                        ErrorData(
                                            msg = errorMsg,
                                        )
                                    )
                                }
                            }
                        }

                    }
            }.getOrElse { _ ->
                _uiState.update {
                    PokemonDetailsUiState.Error(
                        ErrorData(
                            msg = errorMessage,
                        )
                    )
                }
                _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(errorMessage))
            }
        }
    }

    fun onUiEvent(event: PokemonDetailsUiEvents) {
        if (event is PokemonDetailsUiEvents.OnRetryButtonClicked) {
            getPokemonDetails(id = event.id, errorMessage = event.errorMessage)
        }
    }

    companion object {
        const val POKEMON_DETAILS_DATA_BUNDLE_KEY = "POKEMON_DETAILS_DATA_BUNDLE_KEY"
    }
}
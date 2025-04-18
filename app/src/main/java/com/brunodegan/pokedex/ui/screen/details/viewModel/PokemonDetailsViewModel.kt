package com.brunodegan.pokedex.ui.screen.details.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunodegan.pokedex.base.errors.ErrorData
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
import com.brunodegan.pokedex.domain.getPokemonDetailsById.GetPokemonDetailsByIdUseCase
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import com.brunodegan.pokedex.ui.screen.details.PokemonDetailsUiEvents
import com.brunodegan.pokedex.ui.screen.details.PokemonDetailsUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PokemonViewModelDetails(
    private val useCase: GetPokemonDetailsByIdUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _snackbarState = Channel<SnackbarUiStateHolder>()
    val snackbarState = _snackbarState.receiveAsFlow()

    private val _uiState =
        MutableStateFlow<PokemonDetailsUiState>(PokemonDetailsUiState.Initial)
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
                            errorMsg = errorMessage,
                        )
                    )
                }
                _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(errorMessage))
                return@launch
            }

            val savedData =
                savedStateHandle.get<PokemonDetailsViewData>(POKEMON_DETAILS_DATA_BUNDLE_KEY)

            if (savedData == null) {
                runCatching {
                    useCase(id = id)
                        .onStart {
                            _uiState.update { PokemonDetailsUiState.Loading }
                        }.collectLatest { response ->
                            if (response != null) {
                                _uiState.update { PokemonDetailsUiState.Success(response) }
                            }
                        }
                }.getOrElse { _ ->
                    _uiState.update {
                        PokemonDetailsUiState.Error(
                            ErrorData(
                                errorMsg = errorMessage,
                            )
                        )
                    }
                    _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(errorMessage))
                }
            } else {
                _uiState.update { PokemonDetailsUiState.Success(savedData) }
            }
        }
    }

    fun onEvent(event: PokemonDetailsUiEvents) {
        if (event is PokemonDetailsUiEvents.OnRetryButtonClicked) {
            getPokemonDetails(id = event.id, errorMessage = event.errorMessage)
        }
    }

    companion object {
        const val POKEMON_DETAILS_DATA_BUNDLE_KEY = "POKEMON_DETAILS_DATA_BUNDLE_KEY"
    }
}
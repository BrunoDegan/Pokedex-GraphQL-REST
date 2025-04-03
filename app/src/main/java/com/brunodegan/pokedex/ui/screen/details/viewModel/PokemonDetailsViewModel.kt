package com.brunodegan.pokedex.ui.screen.details.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunodegan.pokedex.base.errors.ErrorData
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
import com.brunodegan.pokedex.domain.getPokemonDetailsById.GetPokemonDetailsByIdUseCase
import com.brunodegan.pokedex.ui.screen.details.PokemonDetailsUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PokemonViewModelDetails(
    private val getPokemonDetailsByIdUseCase: GetPokemonDetailsByIdUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _snackbarState = MutableSharedFlow<SnackbarUiStateHolder>()
    val snackbarState = _snackbarState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    private val _uiState =
        MutableStateFlow<PokemonDetailsUiState>(PokemonDetailsUiState.Initial)
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PokemonDetailsUiState.Initial,
    )

    fun getPokemonDetails(id: String? = null, errorMessage: String) {
        viewModelScope.launch {

            // Fail fast principle applied
            if (id.isNullOrEmpty()) {
                _uiState.update {
                    PokemonDetailsUiState.Error(
                        ErrorData(
                            errorMsg = errorMessage,
                        )
                    )
                }
                _snackbarState.emit(SnackbarUiStateHolder.SnackbarUi(errorMessage))
                return@launch
            }

            runCatching {
                getPokemonDetailsByIdUseCase.invoke(id = id)
                    .onStart {
                        _uiState.update { PokemonDetailsUiState.Loading }
                    }.collectLatest { response ->
                        if (response != null) {
                            savedStateHandle[POKEMON_DETAILS_DATA_BUNDLE_KEY] = response
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
                _snackbarState.emit(SnackbarUiStateHolder.SnackbarUi(errorMessage))
            }
        }
    }

    companion object {
        const val POKEMON_DETAILS_DATA_BUNDLE_KEY = "POKEMON_DETAILS_DATA_BUNDLE_KEY"
    }
}
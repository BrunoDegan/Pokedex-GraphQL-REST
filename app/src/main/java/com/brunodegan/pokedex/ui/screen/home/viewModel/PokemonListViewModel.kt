package com.brunodegan.pokedex.ui.screen.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunodegan.pokedex.base.errors.ErrorData
import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
import com.brunodegan.pokedex.domain.getPokemonsUseCase.GetPokemonsDataUseCase
import com.brunodegan.pokedex.ui.mappers.PokemonListViewDataMapper
import com.brunodegan.pokedex.ui.screen.home.events.PokemonListUiEvents
import com.brunodegan.pokedex.ui.screen.home.state.PokemonListUiState
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
class PokemonListViewModel(
    private val useCase: GetPokemonsDataUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mapper: PokemonListViewDataMapper
) : ViewModel() {

    private val _snackbarState = Channel<SnackbarUiStateHolder>()
    val snackbarState = _snackbarState.receiveAsFlow()

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Initial)
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PokemonListUiState.Initial,
    )

    fun onUiEvent(event: PokemonListUiEvents, onCardClicked: (Int) -> Unit) {
        when (event) {
            is PokemonListUiEvents.OnRetryButtonClickedUiEvent -> {
                val errorMessage = event.errorMessage
                getPokemons(errorMessage)
            }

            is PokemonListUiEvents.OnPokemonClickedUiEvent -> {
                val pokemonId = event.id
                onCardClicked(pokemonId)
            }
        }
    }

    fun getPokemons(errorMessage: String) {
        viewModelScope.launch {
            useCase().flowOn(dispatcher)
                .onStart { _uiState.update { PokemonListUiState.Loading } }
                .catch {
                    _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(errorMessage))
                }.collectLatest { responseData ->
                    when (responseData) {
                        is NetworkResponse.Error -> {
                            _uiState.update {
                                PokemonListUiState.Error(
                                    ErrorData(
                                        msg = errorMessage,
                                    )
                                )
                            }
                        }

                        is NetworkResponse.Success -> {
                            _uiState.update {
                                PokemonListUiState.Success(viewData = mapper.map(responseData.data))
                            }
                        }
                    }
                }
        }
    }
}
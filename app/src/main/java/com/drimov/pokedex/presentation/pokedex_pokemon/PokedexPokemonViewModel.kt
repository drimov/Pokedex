package com.drimov.pokedex.presentation.pokedex_pokemon

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.domain.use_case.GetPokemon
import com.drimov.pokedex.util.Resource
import com.drimov.pokedex.util.ResourceMultiple
import com.drimov.pokedex.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokedexPokemonViewModel @Inject constructor(
    private val getPokemon: GetPokemon,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(PokedexPokemonInfoState())
    val state: State<PokedexPokemonInfoState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var pokemon by mutableStateOf<Pokemon?>(null)

    private var loadJob: Job? = null

    init {
        val pokemonId = savedStateHandle.get<Int>("id")
        if (pokemonId != null) {
            getPokemonInfo(pokemonId)
        }
    }

    fun onEvent(event: PokedexPokemonEvent) {
        when (event) {
            is PokedexPokemonEvent.OnPressBack -> {
                sendUiEvent(UiEvent.PopBackStack)
            }
        }
    }

    private fun getPokemonInfo(id: Int?) {
        loadJob = viewModelScope.launch {
            getPokemon.invoke(id!!)
                .onEach { item ->
                    when (item) {
                        is ResourceMultiple.Success -> {
                            _state.value = state.value.copy(
                                pokemon = item.dataT,
                                pokemonSpecies = item.dataY,
                                isLoading = false
                            )
                            this@PokedexPokemonViewModel.pokemon = _state.value.pokemon
                        }
                        is ResourceMultiple.Loading -> {
                            _state.value = state.value.copy(
                                pokemon = item.dataT,
                                pokemonSpecies = item.dataY,
                                isLoading = true
                            )
                        }
                        is ResourceMultiple.Error -> {
                            _state.value = state.value.copy(
                                pokemon = item.dataT,
                                pokemonSpecies = item.dataY,
                                isLoading = false
                            )
                            _uiEvent.emit(
                                UiEvent.ShowSnackBar(
                                    item.message ?: "Unknown Error"
                                )
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
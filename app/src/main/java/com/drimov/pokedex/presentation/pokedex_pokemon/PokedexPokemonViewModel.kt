package com.drimov.pokedex.presentation.pokedex_pokemon

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.domain.use_case.GetPokemon
import com.drimov.pokedex.presentation.pokedex_list.PokedexListEvent
import com.drimov.pokedex.util.Resource
import com.drimov.pokedex.util.Routes
import com.drimov.pokedex.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        val pokemonName = savedStateHandle.get<String>("name")
        val pokemonUrl = savedStateHandle.get<String>("url")
        if (pokemonName != null) {
            getPokemonInfo(pokemonName, pokemonUrl)
        }
    }
//
//    fun onEvent(event: PokedexPokemonEvent) {
//        when (event) {
//            is PokedexPokemonEvent.OnPressBack -> {
//                sendUiEvent(UiEvent.PopBackStack)
//            }
//        }
//    }

    private fun getPokemonInfo(name: String?, url: String?) {
        loadJob = viewModelScope.launch {
            getPokemon.invoke(name!!)
                .onEach { item ->
                    when (item) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                pokemon = item.data,
                                url = url,
                                isLoading = false
                            )
                            this@PokedexPokemonViewModel.pokemon = _state.value.pokemon
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                pokemon = item.data,
                                url = url,
                                isLoading = true
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                pokemon = item.data,
                                url = url,
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
//
//    private fun sendUiEvent(event: UiEvent) {
//        viewModelScope.launch {
//            _uiEvent.emit(event)
//        }
//    }
}
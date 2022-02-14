package com.drimov.pokedex.presentation.pokedex_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drimov.pokedex.domain.model.PokedexListEntry
import com.drimov.pokedex.domain.use_case.GetPokemonList
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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokedexListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonList
) : ViewModel() {


    private val _state = mutableStateOf(PokedexListInfoState())
    val state: State<PokedexListInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var defaultGen = "generation-i"


    init {
        onLoadPokedex(defaultGen)
    }

    private var loadJob: Job? = null

    fun onEvent(event: PokedexListEvent) {
        when (event) {
            is PokedexListEvent.OnPokemonClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.POKEDEX_POKEMON + "?id=${event.id}"))
            }
            is PokedexListEvent.OnGenerationClick -> {
                var gen = event.gen.lowercase().replace(" ", "-")
                onLoadPokedex(gen)
            }
            is PokedexListEvent.OnReload ->{
                onLoadPokedex(defaultGen)
            }
        }
    }

    private fun onLoadPokedex(gen: String) {
        loadJob = viewModelScope.launch {
            getPokemonList.invoke(gen)
                .onEach { item ->
                    when (item) {
                        is Resource.Success -> {
                            val pokedexListEntry =
                                item.data?.pokemon_species?.mapIndexed { index, entry ->
                                    val id = if (entry.url.endsWith("/")) {
                                        entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                                    } else {
                                        entry.url.takeLastWhile { it.isDigit() }
                                    }
                                    val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
                                        PokedexListEntry(
                                            entry.name.replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase(
                                                    Locale.FRANCE
                                                ) else it.toString()
                                            },
                                            url,
                                            id = id.toInt()
                                        )
                                }
                            _state.value = state.value.copy(
                                pokemonItem = pokedexListEntry!!,
                                isLoading = false,
                                isError = false

                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                pokemonItem = emptyList(),
                                isLoading = true,
                                isError = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                pokemonItem = emptyList(),
                                isLoading = false,
                                isError = true
                            )
                            _eventFlow.emit(
                                UiEvent.NetworkError
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}
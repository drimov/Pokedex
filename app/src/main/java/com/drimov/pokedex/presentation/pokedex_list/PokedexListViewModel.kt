package com.drimov.pokedex.presentation.pokedex_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drimov.pokedex.domain.model.PokedexListEntry
import com.drimov.pokedex.domain.use_case.GetPokemonList
import com.drimov.pokedex.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
                onLoadPokedex(event.gen)
            }

            is PokedexListEvent.OnReload ->{
                if(event.gen.isNullOrEmpty()) onLoadPokedex(defaultGen)
                else onLoadPokedex(event.gen)

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
                                item.data?.pokemon_species?.mapIndexed { _, entry ->
                                    val id = entry.url.digit("/")
                                    val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
                                        PokedexListEntry(
                                            entry.name.ucFirst(),
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
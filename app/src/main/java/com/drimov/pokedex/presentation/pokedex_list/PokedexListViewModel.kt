package com.drimov.pokedex.presentation.pokedex_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drimov.pokedex.domain.model.PokedexListEntry
import com.drimov.pokedex.domain.use_case.GetPokemonList
import com.drimov.pokedex.domain.use_case.GetStringPrefs
import com.drimov.pokedex.domain.use_case.PutStringPrefs
import com.drimov.pokedex.util.*
import com.drimov.pokedex.util.Constants.KEY_STORE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokedexListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonList,
    private val getStringPrefs: GetStringPrefs,
    private val putStringPrefs: PutStringPrefs
) : ViewModel() {


    private val _state = mutableStateOf(PokedexListInfoState())
    val state: State<PokedexListInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _language = MutableStateFlow("en")
    val language = _language.asStateFlow()

    private var defaultGen = "generation-i"


    init {
        getLanguage(language.value)
        onLoadPokedex(defaultGen, language.value)
    }

    private var loadJob: Job? = null

    fun onEvent(event: PokedexListEvent) {
        when (event) {
            is PokedexListEvent.OnPokemonClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.POKEDEX_POKEMON + "?id=${event.id}?language=${event.language}"))
            }
            is PokedexListEvent.OnGenerationClick -> {
                onLoadPokedex(event.gen, language.value)
            }
            is PokedexListEvent.OnLanguageClick -> {
                setLanguage(event.language)
                _language.value = event.language
            }

            is PokedexListEvent.OnReload -> {
                if (event.gen.isNullOrEmpty()) onLoadPokedex(defaultGen, language.value)
                else {
                    onLoadPokedex(event.gen, language.value)
                    defaultGen = event.gen
                }
            }
        }
    }

    private fun setLanguage(language: String) {
        loadJob = viewModelScope.launch {
            putStringPrefs.invoke(KEY_STORE, language)
        }
    }

    private fun getLanguage(defaultLanguage: String) {

        loadJob = viewModelScope.launch {
            getStringPrefs.invoke().collect { entryLanguage ->
                when (entryLanguage) {
                    is Resource.Success -> {
                        if (entryLanguage.data.isNullOrEmpty()) {
                            setLanguage(defaultLanguage)
                        } else {
                            _language.value = entryLanguage.data
                        }
                    }
                    is Resource.Error -> Unit
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    private fun onLoadPokedex(gen: String, currentLanguage: String) {
        loadJob = viewModelScope.launch {
            getPokemonList.invoke(gen, currentLanguage)
                .onEach { item ->
                    when (item) {
                        is Resource.Success -> {
                            val pokedexListEntry =
                                item.data?.pokemon_species?.mapIndexed { _, entry ->
                                    val id = entry.url.digit("/")
                                    val url =
                                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
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
                                isError = true,
                                message = item.message
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
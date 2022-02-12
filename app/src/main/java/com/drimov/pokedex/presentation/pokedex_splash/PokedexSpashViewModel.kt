package com.drimov.pokedex.presentation.pokedex_splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drimov.pokedex.util.Routes
import com.drimov.pokedex.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokedexSpashViewModel @Inject constructor(): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        onAutoNavigate()
    }

    private fun onAutoNavigate() {
        viewModelScope.launch {
            delay(2000)
            _uiEvent.send(UiEvent.Navigate(Routes.POKEDEX_LIST))
        }
    }
}
package com.drimov.pokedex.presentation.pokedex_splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.drimov.pokedex.R
import com.drimov.pokedex.presentation.ui.theme.Blue700
import com.drimov.pokedex.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun PokedexSplashScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: PokedexSpashViewModel = hiltViewModel()
) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.6f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(5f).getInterpolation(it)
                }
            )
        )
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Blue700)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_pokeapi_500),
            contentDescription = "logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PokedexSplashPreview() {
    PokedexSplashScreen(onNavigate = {}, viewModel = PokedexSpashViewModel())
}
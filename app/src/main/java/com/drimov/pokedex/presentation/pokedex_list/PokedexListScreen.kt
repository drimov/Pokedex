package com.drimov.pokedex.presentation.pokedex_list

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.drimov.pokedex.domain.use_case.GetPokemonList
import com.drimov.pokedex.util.UiEvent
import kotlinx.coroutines.flow.collect
import com.drimov.pokedex.R
import com.drimov.pokedex.presentation.ui.theme.Blue700
import com.drimov.pokedex.presentation.ui.theme.Red200

@Composable
fun PokedexListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: PokedexListViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    var expanded by remember { mutableStateOf(false) }
    var selectItem by remember { mutableStateOf(0) }
    val items = listOf<String>(
        "Generation I",
        "Generation II",
        "Generation III",
        "Generation IV",
        "Generation V",
        "Generation VI",
        "Generation VII",
        "Generation VIII",
    )
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                expanded = !expanded
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "setting"
                )
            }
        }
    ) {
        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Blue700)
                    .clip(shape = RoundedCornerShape(16.dp))
            ) {
                items.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        onClick = {
                            Log.d("Drop", "${index.plus(1)}")
                            viewModel.onEvent(PokedexListEvent.OnGenerationClick(text,index.plus(1)))
                        }) {
                        Text(text = text, color = Color.White)
                    }
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Blue700)
                .padding(16.dp)

        ) {
            items(state.pokemonItem.size) { i ->
                val pokemon = state.pokemonItem[i]
                if (i > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                PokemonInfo(
                    pokedexListEntry = pokemon,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            viewModel.onEvent(PokedexListEvent.OnPokemonClick(pokemon.name.lowercase(), url = pokemon.url))
                        }
                )
                if (i < state.pokemonItem.size - 1) {
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
        //-------------------//
        if (state.isLoading) {
            Log.d("list screen", "isLoading end")
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }

        }

    }
    BackHandler(enabled = true) {}
}

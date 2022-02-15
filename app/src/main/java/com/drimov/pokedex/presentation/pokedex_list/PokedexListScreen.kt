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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.drimov.pokedex.util.UiEvent
import kotlinx.coroutines.flow.collect
import com.drimov.pokedex.presentation.ui.theme.Blue700
import com.drimov.pokedex.presentation.ui.theme.Red200
import com.drimov.pokedex.util.Constants
import com.drimov.pokedex.util.Constants.genList

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

    val state by remember {
        viewModel.state
    }
    val scaffoldState = rememberScaffoldState()
    var expanded by remember { mutableStateOf(false) }


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
                    .background(Blue700),
            ) {
                genList.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        onClick = {
                            viewModel.onEvent(
                                PokedexListEvent.OnGenerationClick(
                                    text,
                                    index.plus(1)
                                )
                            )
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
                .padding(16.dp),

            ) {
            val pokemonFilter = state.pokemonItem.sortedBy {
                it.id
            }
            items(pokemonFilter.size) { i ->
                val pokemon = pokemonFilter[i]
                if (i > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                PokemonInfo(
                    pokedexListEntry = pokemon,
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize(),
                    id = pokemonFilter[i].id
                )
                if (i < state.pokemonItem.size - 1) {
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
        if (state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
        if (state.isError) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Center)
            ) {
                Text(
                    text = Constants.httpExceptionErr,
                    color = Color.Red,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Box (modifier = Modifier.align(CenterHorizontally)) {
                    Button(
                        onClick = { viewModel.onEvent(PokedexListEvent.OnReload)}
                    ) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
    BackHandler(enabled = true) {}
}
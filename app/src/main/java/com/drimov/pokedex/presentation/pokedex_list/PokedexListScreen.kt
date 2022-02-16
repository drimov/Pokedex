package com.drimov.pokedex.presentation.pokedex_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.drimov.pokedex.R
import com.drimov.pokedex.util.UiEvent
import kotlinx.coroutines.flow.collect
import com.drimov.pokedex.presentation.ui.theme.Blue700
import com.drimov.pokedex.presentation.ui.theme.Red200
import com.drimov.pokedex.util.Constants
import com.drimov.pokedex.util.Constants.nbGen
import com.drimov.pokedex.util.parseGeneration
import com.drimov.pokedex.util.parseNbToRomanNb

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
    var genLoad: String by rememberSaveable { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(Red200),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                expanded = !expanded
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.content)
                )
            }
        }
    ) {
        if (expanded) {
            DropMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded },
                modifier = Modifier,
                viewModel = viewModel,
                genLoad = { genLoad = it }
            )
        }
        PokemonList(modifier = Modifier, state = state, viewModel = viewModel)
        HandleState(modifier = Modifier, state = state, viewModel = viewModel, gen = genLoad)
    }
    BackHandler(enabled = true) {}
}

@Composable
fun DropMenu(
    expanded: Boolean,
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    viewModel: PokedexListViewModel,
    genLoad : (String) -> Unit
) {

    Box(modifier = modifier, contentAlignment = TopStart) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier
                .background(Blue700),
        ) {
            for (i in 1..nbGen) {
                val gen = parseNbToRomanNb(i)
                val genParse =  parseGeneration(gen)
                DropdownMenuItem(
                    onClick = {
                        genLoad(genParse)
                        viewModel.onEvent(
                            PokedexListEvent.OnGenerationClick(genParse)
                        )
                    }) {
                    Text(text = gen, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun PokemonList(modifier: Modifier, state: PokedexListInfoState, viewModel: PokedexListViewModel) {
    LazyColumn(
        modifier = modifier
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
                Spacer(modifier = modifier.height(8.dp))
            }
            PokemonInfo(
                pokedexListEntry = pokemon,
                viewModel = viewModel,
                modifier = modifier
                    .fillMaxSize(),
                id = pokemonFilter[i].id
            )
            if (i < state.pokemonItem.size - 1) {
                Spacer(modifier = modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun HandleState(modifier: Modifier, state: PokedexListInfoState, viewModel: PokedexListViewModel,gen: String?) {
    if (state.isLoading) {
        Box(
            contentAlignment = Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
    if (state.isError) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize(Center)
        ) {
            Text(
                text = Constants.httpExceptionErr,
                color = Red200,
                fontSize = 18.sp,
                modifier = modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = modifier.padding(8.dp))
            Box(modifier = modifier.align(CenterHorizontally)) {
                Button(
                    onClick = { viewModel.onEvent(PokedexListEvent.OnReload(gen)) }
                ) {
                    Text(stringResource(id = R.string.retry))
                }
            }
        }
    }
}
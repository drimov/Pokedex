package com.drimov.pokedex.presentation.pokedex_list

import android.graphics.drawable.VectorDrawable
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.ImagePainter
import com.drimov.pokedex.R
import com.drimov.pokedex.presentation.ui.theme.Blue700
import com.drimov.pokedex.presentation.ui.theme.Red200
import com.drimov.pokedex.util.*
import com.drimov.pokedex.util.Constants.nbGen
import kotlinx.coroutines.flow.collect
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
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
    var expandedGen by remember { mutableStateOf(false) }
    var expandedLanguage by remember { mutableStateOf(false) }
    var genLoad: String by rememberSaveable { mutableStateOf("") }
//    val currentLanguage by remember { mutableStateOf(viewModel.language) }
    val currentLanguage = viewModel.language.collectAsState()

    SetLanguage(language = currentLanguage.value)

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(Red200),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                expandedGen = !expandedGen
            },backgroundColor = Red200) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.content),
                    tint = Color.White
                )
            }
//            val drawable = LocalContext.current.resources.getDrawable(R.drawable.ic_baseline_translate_24) as ImageVector
            FloatingActionButton(onClick = {
                expandedLanguage = !expandedLanguage
            }, backgroundColor = Red200, modifier = Modifier.offset(y = ((-75).dp))) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_translate_24),
                    contentDescription = stringResource(
                        id = R.string.language,
                    ),
                )
//                Icon(
//                    imageVector = Icons.Filled.t,
//                    contentDescription = stringResource(id = R.string.language)
//                )
            }
        }
    ) {
        if (expandedGen) {
            DropMenuGen(
                expanded = expandedGen,
                onDismissRequest = { expandedGen = !expandedGen },
                modifier = Modifier,
                viewModel = viewModel,
                genLoad = { genLoad = it }
            )
        }
        if (expandedLanguage) {
            DropMenuLoad(
                expanded = expandedLanguage,
                onDismissRequest = { expandedLanguage = !expandedLanguage },
                modifier = Modifier,
                viewModel = viewModel,
//                languageLoad = { currentLanguage = it }
            )
        }
        PokemonList(modifier = Modifier, state = state, viewModel = viewModel)
        HandleState(modifier = Modifier, state = state, viewModel = viewModel, gen = genLoad)
    }
    BackHandler(enabled = true) {}
}

@Composable
fun DropMenuLoad(
    expanded: Boolean,
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    viewModel: PokedexListViewModel,
//    languageLoad: (String) -> Unit
) {
    val listLanguages = mapOf<String, String>(
        "en" to stringResource(id = R.string.eng_language),
        "es" to stringResource(id = R.string.es_language),
        "fr" to stringResource(id = R.string.fr_language),
        "it" to stringResource(id = R.string.it_language),
        "ja" to stringResource(id = R.string.ja_language),
        "ko" to stringResource(id = R.string.ko_language),

        )

    Box(modifier = modifier, contentAlignment = TopStart) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier
                .background(Blue700),
        ) {
            listLanguages.entries.forEach { entry ->
                DropdownMenuItem(
                    onClick = {
//                        languageLoad(entry.key)
                        viewModel.onEvent(
                            PokedexListEvent.OnLanguageClick(entry.key)
                        )
                    }) {
                    Text(
                        text = entry.value,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun DropMenuGen(
    expanded: Boolean,
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    viewModel: PokedexListViewModel,
    genLoad: (String) -> Unit
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
                val genParse = parseGeneration(gen)
                DropdownMenuItem(
                    onClick = {
                        genLoad(genParse)
                        viewModel.onEvent(
                            PokedexListEvent.OnGenerationClick(genParse)
                        )
                    }) {
                    Text(
                        text = gen,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SetLanguage(language: String) {

    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = LocalConfiguration.current
    config.setLocale(locale)
    val resources = LocalContext.current.resources
    resources.updateConfiguration(config, resources.displayMetrics)
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
fun HandleState(
    modifier: Modifier,
    state: PokedexListInfoState,
    viewModel: PokedexListViewModel,
    gen: String?
) {
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
            var message = ""
            when (state.message) {
                Constants.httpExceptionErr -> message =
                    stringResource(id = R.string.http_exception_err)
                Constants.ioExceptionErr -> message = stringResource(id = R.string.io_exception_err)

            }
            Text(
                text = message,
                color = Red200,
                fontSize = 18.sp,
                modifier = modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.padding(8.dp))
            Box(modifier = modifier.align(CenterHorizontally)) {
                val retry = stringResource(id = R.string.retry)
                Button(
                    onClick = { viewModel.onEvent(PokedexListEvent.OnReload(gen)) }
                ) {
                    Text(text = retry)
                }
            }
        }
    }
}
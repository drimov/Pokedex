package com.drimov.pokedex.presentation.pokedex_pokemon

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.drimov.pokedex.R
import com.drimov.pokedex.data.remote.dto.*
import com.drimov.pokedex.presentation.ui.theme.*
import com.drimov.pokedex.util.UiEvent
import com.drimov.pokedex.util.parseNumberToIndex
import com.drimov.pokedex.util.parseTypeToColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PokedexPokemonScreen(
    onPopBackStack: () -> Unit,
    viewModel: PokedexPokemonViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }
    val defaultLanguage = "en"
    val dominantType = 0
    var backgroundColor: Color? = null
    when (viewModel.state.value.isLoading) {
        true -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Center)
                )
            }
        }
        false -> {
            backgroundColor = parseTypeToColor(viewModel.state.value.pokemon!!.types[dominantType])
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor!!)
                    .verticalScroll(scroll, true),

                ) {

                val titles: List<String> = listOf("About", "Stats", "Others")
                HeaderInfo(
                    viewModel = viewModel,
//                    color = backgroundColor,
                    language = defaultLanguage
                )
                CardContent(
                    pages = titles,
                    pokemon = viewModel.state.value.pokemon!!,
                    pokemonSpecies = viewModel.state.value.pokemonSpecies!!,
                    language = defaultLanguage
                )
            }
        }
    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HeaderInfo(viewModel: PokedexPokemonViewModel, language: String) {
    val pokemon = viewModel.state.value.pokemon
    val pokemonSpecies = viewModel.state.value.pokemonSpecies
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(0.3f)
        ) {
            //arrow
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_keyboard_backspace_24),
                contentDescription = "arrow back",
                modifier = Modifier
                    .scale(1.5f)
                    .clickable {
                        viewModel.onEvent(PokedexPokemonEvent.OnPressBack)
                    },
                tint = Color.White
            )

            Text(
                text = parseNumberToIndex(pokemon!!.id),
                modifier = Modifier
                    .alpha(0.60f),
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )

            // name
            var indexName: Int? = null
            pokemonSpecies!!.names.forEachIndexed { index, name ->
                if (name.language.name == language) {
                    indexName = index
                }
            }
            Text(
                text = pokemonSpecies.names[indexName!!].name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                },
                modifier = Modifier,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp
            )
            // images types
            Row(
                modifier = Modifier
            ) {
                pokemon?.types?.forEach {

                    val painter = rememberImagePainter(
                        data = "https://raw.githubusercontent.com/itsjavi/pokemon-assets/master/assets/img/symbols/type-${it.type.name}-badge-masters.png",
                    )
                    Image(
                        painter = painter,
                        contentDescription = "type-${it.type.name}",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 12.dp)
                    )
                }
            }
        }
        //type pokemon
        var idGenera: Int? = null
        pokemonSpecies?.genera?.forEachIndexed { index, genera ->
            if (genera.language.name == language) {
                idGenera = index
            }
        }
        val textGenera = pokemonSpecies!!.genera[idGenera!!]
        Text(
            text = textGenera.genus,
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(0.3f)
                .padding(top = 32.dp)
                .padding(16.dp),
            color = Color.White
        )
    }
    Row {
        //image
        val painter = rememberImagePainter(
            data = viewModel.state.value.pokemon!!.sprites.other.officialArtwork.front_default
        )
        val painterState = painter.state

        Image(
            painter = painter,
            contentDescription = pokemon?.name,
            modifier = Modifier
                .size(210.dp)
                .weight(0.40f),
            alignment = BottomCenter
        )
        if (painterState is ImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardContent(
    pages: List<String>,
    pokemon: Pokemon,
    pokemonSpecies: PokemonSpecies,
    language: String
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            Modifier.pagerTabIndicatorOffset(
                pagerState = pagerState,
                tabPositions = tabPositions
            )
        },
        modifier = Modifier
            .padding(top = 15.dp),
        backgroundColor = Color.Transparent
    ) {
        // Adds tabs for all of our pages
        pages.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                },
                text = {
                    Text(text = title, color = Color.White)
                }
            )
        }
    }
    HorizontalPager(
        count = pages.size,
        state = pagerState,
        verticalAlignment = Alignment.Top,
        reverseLayout = true
    ) { page ->
        when (page) {
            0 -> AboutCard(
                pokemon = pokemon,
                pokemonSpecies = pokemonSpecies,
                language = language
            )
            1 -> StatsCard(stats = pokemon.stats)
            2 -> OthersCard(abilities = pokemon.abilities, pokemonSpecies = pokemonSpecies)
        }
    }
}

@Composable
fun OthersCard(abilities: List<Ability>, pokemonSpecies: PokemonSpecies) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(modifier = Modifier.padding(8.dp).fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize(0.4f)) {
                Text(
                    text = "Base happiness",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Habitat",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Growth rate",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Shape",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Abilities",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier.padding(16.dp).fillMaxSize(0.65f)) {

                Text(
                    text = pokemonSpecies.base_happiness.toString() ?: "unknown",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Text(
                    text = pokemonSpecies.habitat?.name ?: "unknown",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Text(
                    text = pokemonSpecies.growth_rate.name ?: "unknown",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Text(
                    text = pokemonSpecies.shape.name ?: "unknown",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column {
                        abilities.forEach {
                            Text(
                                text = it.ability.name,
                                color = Color.White,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AboutCard(pokemon: Pokemon, pokemonSpecies: PokemonSpecies, language: String) {

    var indexLanguage: Int? = null
    pokemonSpecies.flavor_text_entries.forEachIndexed { index, flavor_entry ->
        if (flavor_entry.language.name == language) {
            indexLanguage = index
        }
    }
    val text = pokemonSpecies.flavor_text_entries[indexLanguage!!].flavor_text.replace("\n", "")
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row {
            Text(
                text = text,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = Color.White
            )
        }
        //Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
                .padding(horizontal = 8.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(15.dp)),
            shape = RoundedCornerShape(15.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val footToM = 3.281
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Height",
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alpha(0.8f),
                        color = Grey200
                    )
                    val calcHeight = pokemon.height / footToM
                    val calcRef =
                        String.format(Locale.FRANCE, "%.2f", calcHeight)
                    Text(text = "${pokemon.height} \" (${calcRef} m)")
                }
                val lbsToKg = 2.205
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Weight",
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alpha(0.8f),
                        color = Grey200
                    )
                    val calcWeight =
                        String.format(Locale.FRANCE, "%.2f", pokemon.weight / lbsToKg)
                    Text(text = "${pokemon.weight} lbs (${calcWeight} kg)")
                }
            }

        }
        // Breeding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // title
                Row(
                    modifier = Modifier
                ) {
                    Text(
                        text = "Breeding", modifier = Modifier.padding(4.dp), color = Color.White
                    )
                }
                // objects
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // title object
                    Column(modifier = Modifier.padding(4.dp)) {
                        Text(text = "Egg Groups", color = Color.White)
                        Text(text = "Capture Rate", color = Color.White)
                    }
                    // value object
                    Column(modifier = Modifier.padding(4.dp)) {
                        Row {
                            var nbEggGroup = 0
                            pokemonSpecies.egg_groups.forEach { egg ->
                                Text(text = egg.name, color = Color.White)
                                if (nbEggGroup != (pokemonSpecies.egg_groups.size - 1)) {
                                    Text(text = ", ", color = Color.White)
                                    nbEggGroup++
                                }
                            }
                            if (pokemonSpecies.egg_groups.isEmpty()) {
                                Text(text = "Unknown", color = Color.White)
                            }
                        }
                        Text(
                            text = "${pokemonSpecies.capture_rate}",
                            color = Color.White
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun StatsCard(stats: List<Stat>) {

    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        //Property
        Column(modifier = Modifier.weight(0.35f)) {
            stats.forEach {
                Text(
                    text = it.stat.name.replaceFirstChar { letter ->
                        if (letter.isLowerCase()) letter.titlecase(Locale.FRANCE) else letter.toString()
                    },
                    modifier = Modifier.padding(6.dp),
                    color = Color.White
                )
            }
        }
        // Values
        Column(modifier = Modifier.weight(0.15f)) {
            stats.forEach {
                Text(
                    text = it.base_stat.toString(),
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
        // Progress Bar
        Column(modifier = Modifier.weight(0.5f)) {
            stats.forEach {
                val baseStat = it.base_stat / 150.toFloat()
                var colorBar: Color? = null

                colorBar = when (it.stat.name) {
                    "hp", "defense", "speed" -> Blue700
                    else -> Color.White
                }
                LinearProgressIndicator(
                    progress = baseStat,
                    modifier = Modifier.padding(15.dp),
                    color = colorBar!!
                )
            }
        }
    }
}
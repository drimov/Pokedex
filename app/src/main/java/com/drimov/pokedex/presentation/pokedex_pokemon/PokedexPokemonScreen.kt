package com.drimov.pokedex.presentation.pokedex_pokemon

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.drimov.pokedex.R
import com.drimov.pokedex.data.remote.dto.AbilityTranslate
import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.data.remote.dto.PokemonSpecies
import com.drimov.pokedex.domain.model.PokemonData
import com.drimov.pokedex.presentation.ui.theme.Blue700
import com.drimov.pokedex.presentation.ui.theme.Grey200
import com.drimov.pokedex.presentation.ui.theme.Red200
import com.drimov.pokedex.util.*
import com.drimov.pokedex.util.Constants.footToMeter
import com.drimov.pokedex.util.Constants.lbsToKg
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun PokedexPokemonScreen(
    onPopBackStack: () -> Unit,
    viewModel: PokedexPokemonViewModel = hiltViewModel()
) {
    // jap  ja-Hrkt
    // ko korean
    val defaultLanguage = viewModel.language
    var language = defaultLanguage.value
   when(language){
       "ja" -> language = "ja-Hrkt"
   }
    val color = Color.White
    val fontSize = 18.sp

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }
//    val state = viewModel.state.value
//    HandleState(modifier = Modifier, state = state, viewModel = viewModel, color = color, fontSize = fontSize, defaultLanguage = defaultLanguage)

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

            PokemonDetails(
                modifier = Modifier,
                viewModel = viewModel,
                color = color,
                fontSize = fontSize,
                language = language
            )
        }
    }
}

@Composable
fun PokemonDetails(
    modifier: Modifier,
    viewModel: PokedexPokemonViewModel,
    color: Color,
    fontSize: TextUnit,
    language: String,
) {
    val dominantType = 0
    val titles: List<String> = listOf(
        stringResource(id = R.string.about),
        stringResource(id = R.string.stats),
        stringResource(id = R.string.others)
    )
    val backgroundColor =
        parseTypeToColor(viewModel.state.value.pokemonData?.pokemon!!.types[dominantType])
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .verticalScroll(scroll, true),

        ) {
        HeaderInfo(
            modifier = modifier,
            viewModel = viewModel,
            language = language,
            color = color,
            fontSize = fontSize
        )
        PagersContent(
            modifier = modifier,
            pages = titles,
            pokemonData = viewModel.state.value.pokemonData!!,
            language = language,
            color = color,
            fontSize = fontSize
        )
    }
}

// -------- HEADER -------- //
@Composable
fun HeaderInfo(
    modifier: Modifier,
    viewModel: PokedexPokemonViewModel,
    language: String,
    color: Color,
    fontSize: TextUnit
) {
    val pokemon = viewModel.state.value.pokemonData!!.pokemon
    val pokemonSpecies = viewModel.state.value.pokemonData!!.pokemonSpecies

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier.weight(0.3f)
        ) {
            //arrow
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_keyboard_backspace_24),
                contentDescription = stringResource(id = R.string.arrow_back),
                modifier = modifier
                    .scale(1.5f)
                    .clickable {
                        viewModel.onEvent(PokedexPokemonEvent.OnPressBack)
                    },
                tint = color
            )
            HeaderPokemonNameAndNumber(
                modifier = modifier,
                color = color,
                pokemonSpecies = pokemonSpecies,
                pokemon = pokemon,
                language = language,
                fontSize = fontSize
            )
            HeaderTypeImg(modifier = modifier, pokemon = pokemon)
        }
        HeaderTypeText(
            modifier = modifier,
            color = color,
            pokemonSpecies = pokemonSpecies,
            language = language
        )
    }
    HeaderPokemonImg(modifier = modifier, viewModel = viewModel, pokemon = pokemon)
}

@Composable
fun HeaderPokemonNameAndNumber(
    modifier: Modifier,
    color: Color,
    pokemonSpecies: PokemonSpecies?,
    pokemon: Pokemon?,
    language: String,
    fontSize: TextUnit
) {

    Text(
        text = parseNumberToIndex(pokemon!!.id),
        modifier = modifier
            .alpha(0.60f),
        color = color,
        fontWeight = FontWeight.ExtraBold,
        fontSize = fontSize
    )

    var idText: Int? = null
    pokemonSpecies!!.names.forEachIndexed { index, name ->
        if (name.language.name == language) {
            idText = index
        }
    }
    Text(
        text = pokemonSpecies.names[idText!!].name.ucFirst(),
        modifier = modifier,
        color = color,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 25.sp
    )
}

@Composable
fun HeaderTypeImg(modifier: Modifier, pokemon: Pokemon?) {
    // images types
    Row(
        modifier = modifier
    ) {
        pokemon!!.types.forEach {
            val painter = rememberImagePainter(
                data = it.type.name.urlType()
            )
            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.type, it.type.name),
                modifier = modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )
        }
    }
}

@Composable
fun HeaderTypeText(
    modifier: Modifier,
    color: Color,
    pokemonSpecies: PokemonSpecies?,
    language: String
) {

    //type pokemon
    var idGenera: Int? = null
    pokemonSpecies!!.genera.forEachIndexed { index, genera ->
        if (genera.language.name == language) idGenera = index
    }
    val textGenera = pokemonSpecies.genera[idGenera!!]
    Text(
        text = textGenera.genus,
        textAlign = TextAlign.End,
        modifier = modifier
//            .weight(0.3f)
            .padding(top = 32.dp)
            .padding(16.dp),
        color = color
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HeaderPokemonImg(modifier: Modifier, viewModel: PokedexPokemonViewModel, pokemon: Pokemon?) {
    Row {
        //image
        val painter = rememberImagePainter(
            data = viewModel.state.value.pokemonData!!.pokemon.sprites.other.officialArtwork.front_default
        )
        val painterState = painter.state

        Image(
            painter = painter,
            contentDescription = pokemon?.name,
            modifier = modifier
                .size(210.dp)
                .weight(0.40f),
            alignment = BottomCenter
        )
        if (painterState is ImagePainter.State.Loading) {
            Box(modifier = modifier.fillMaxSize().size(210.dp)){
                CircularProgressIndicator(modifier = Modifier.align(Center))
            }

        }
    }
}

// -------- PAGERS -------- //
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagersContent(
    modifier: Modifier,
    pages: List<String>,
    pokemonData: PokemonData,
    language: String,
    color: Color,
    fontSize: TextUnit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            modifier.pagerTabIndicatorOffset(
                pagerState = pagerState,
                tabPositions = tabPositions
            )
        },
        modifier = modifier
            .padding(top = 15.dp),
        backgroundColor = Color.Transparent
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                },
                text = {
                    Text(text = title, color = color)
                },
                selectedContentColor = Blue700
            )
        }
    }
    HorizontalPager(
        count = pages.size,
        state = pagerState,
        verticalAlignment = Alignment.Top,
        reverseLayout = true,
        contentPadding = PaddingValues(8.dp)
    ) { page ->
        when (page) {
            0 -> AboutCard(
                modifier = modifier,
                color = color,
                pokemonData = pokemonData,
                language = language
            )
            1 -> StatsCard(
                modifier = modifier,
                pokemonData = pokemonData,
                language = language,
                color = color,
            )
            2 -> OthersCard(
                modifier = modifier,
                color = color,
                pokemonData = pokemonData,
                fontSize = fontSize,
                language = language
            )
        }
    }
}

// -------- ABOUT -------- //
@Composable
fun AboutCard(
    modifier: Modifier,
    pokemonData: PokemonData,
    language: String,
    color: Color
) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Story(
            modifier = modifier,
            color = color,
            pokemonSpecies = pokemonData.pokemonSpecies,
            language = language
        )
        Information(modifier = modifier, pokemon = pokemonData.pokemon)
        Breeding(modifier = modifier, color = color, pokemonData = pokemonData, language = language)
    }
}

@Composable
fun Story(modifier: Modifier, color: Color, pokemonSpecies: PokemonSpecies, language: String) {

    var idText = 0
    pokemonSpecies.flavor_text_entries.forEachIndexed { index, flavorTextEntry ->
        if (flavorTextEntry.language.name == language) idText = index
    }

    val text = pokemonSpecies.flavor_text_entries[idText].flavor_text.replace("\n", "")
    Row {
        Text(
            text = text,
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = color
        )
    }
}

@Composable
fun Information(modifier: Modifier, pokemon: Pokemon) {
    //Card
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(15.dp)),
        shape = RoundedCornerShape(15.dp)

    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = modifier.padding(8.dp))
            InformationHeight(modifier = modifier, pokemon = pokemon)
            InformationWeight(modifier = modifier, pokemon = pokemon)
        }
    }
}

@Composable
fun InformationHeight(modifier: Modifier, pokemon: Pokemon) {
    // Height
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.height),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            modifier = modifier.alpha(0.8f),
            color = Grey200
        )
        val ft = stringResource(id = R.string.feet)
        val m = stringResource(id = R.string.meter)
        val calcRef = calWithRef(number = pokemon.height.toDouble(), ref = footToMeter)
        Text(text = "${pokemon.height}$ft (${calcRef} $m)")
    }
}

@Composable
fun InformationWeight(modifier: Modifier, pokemon: Pokemon) {
    // Weight
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.weight),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            modifier = modifier.alpha(0.8f),
            color = Grey200
        )
        val calcWeight = calWithRef(pokemon.weight.toDouble(), ref = lbsToKg)
        val lbs = stringResource(id = R.string.lbs)
        val kg = stringResource(id = R.string.kg)
        Text(text = "${pokemon.weight} $lbs (${calcWeight} $kg)")
    }
}

@Composable
fun Breeding(modifier: Modifier, color: Color, pokemonData: PokemonData, language: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            BreedingTitle(modifier = modifier, color = color)
            BreedingObject(
                modifier = modifier,
                color = color,
                pokemonData = pokemonData,
                language = language
            )
        }

    }
}

@Composable
fun BreedingTitle(modifier: Modifier, color: Color) {
    // title
    Row {
        Text(
            text = stringResource(id = R.string.breeding),
            modifier = modifier.padding(4.dp),
            color = color
        )
    }
}

@Composable
fun BreedingObject(modifier: Modifier, color: Color, pokemonData: PokemonData, language: String) {
    //object
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // title object
        Column(modifier = modifier.padding(4.dp)) {
            Text(text = stringResource(id = R.string.egg_groups), color = color)
            Text(text = stringResource(id = R.string.capture_rate), color = color)
        }
        // value object
        Column(modifier = modifier.padding(4.dp)) {
            Row {
                var nbEggGroup = 0
                var idEgg = 0
                pokemonData.egg?.forEach {
                    it.names.forEachIndexed { index, name ->
                        if (name.language.name == language) {
                            idEgg = index
                        }
                    }
                    Text(text = it.names[idEgg].name.ucFirst(), color = color)
                    if (nbEggGroup != (pokemonData.egg.size - 1)) {
                        Text(text = ", ", color = color)
                        nbEggGroup++
                    }
                }

                if (pokemonData.egg?.isEmpty() == true) {
                    Text(text = stringResource(id = R.string.unknown), color = color)
                }
            }
            Text(
                text = "${pokemonData.pokemonSpecies.capture_rate}",
                color = color
            )
        }
    }
}

// -------- STATS -------- //

@Composable
fun StatsCard(modifier: Modifier, pokemonData: PokemonData, language: String, color: Color) {
    Row(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val statsTrl = pokemonData.stats
        val stats = pokemonData.pokemon.stats

        //Property
        Column(modifier = modifier.weight(0.35f)) {
            statsTrl.forEach {
                var idStat = 5
                it.names.forEachIndexed { index, name ->
                    if (name.language.name == language) {
                        idStat = index
                    }
                }

                Text(
                    text = it.names[idStat].name.ucFirst(),
                    modifier = modifier.padding(6.dp),
                    color = color,
                    textAlign = TextAlign.Justify
                )
            }
            Text(
                text = stringResource(id = R.string.total),
                modifier = modifier.padding(6.dp),
                color = color
            )
        }
        // Values
        var totalStat = 0
        Column(modifier = modifier.weight(0.10f)) {
            stats.forEach {
                Text(
                    text = it.base_stat.toString(),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = color
                )
                totalStat += it.base_stat
            }
            // Total values
            Text(
                text = totalStat.toString(), modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = color
            )
        }
        // Progress Bar
        Column(modifier = modifier.weight(0.5f)) {
            val maxPerStat = 150
            val totalMaxStats = maxPerStat * stats.size
            stats.forEach {
                val baseStat = it.base_stat / maxPerStat.toFloat()
                val colorBar: Color?

                colorBar = parseStatToColor(it.stat)
                LinearProgressIndicator(
                    progress = baseStat,
                    modifier = modifier.padding(15.dp),
                    color = colorBar
                )
            }
            LinearProgressIndicator(
                progress = totalStat / totalMaxStats.toFloat(),
                modifier = modifier.padding(15.dp),
                color = color
            )
        }
    }
}

// -------- OTHERS -------- //
@Composable
fun OthersCard(
    modifier: Modifier,
    pokemonData: PokemonData,
    color: Color,
    fontSize: TextUnit,
    language: String
) {
    val fontWTitle = FontWeight.Bold
    val fontWContent = FontWeight.Normal

    val abilities = pokemonData.abilities
    val habitat = pokemonData.habitat
    val growRate = pokemonData.growthRate
    val shape = pokemonData.shape
    val baseHappiness = pokemonData.pokemonSpecies.base_happiness

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        var idGrowthRate = 2
        growRate.descriptions.forEachIndexed { index, description ->
            if (description.language.name == language) {
                idGrowthRate = index
            }
        }
        var idHabitat = 2
        habitat?.names?.forEachIndexed { index, name ->
            if (name.language.name == language) {
                idHabitat = index
            }
        }
        var idShape = 1
        shape?.names?.forEachIndexed { index, name ->
            if (name.language.name == language) {
                idShape = index
            }
        }

        val listContent = listOf<String>(
            baseHappiness.toString(),
            habitat?.let { it.names[idHabitat].name } ?: stringResource(id = R.string.unknown),
            growRate.descriptions[idGrowthRate].description,
            shape?.let { it.names[idShape].name } ?: stringResource(id = R.string.unknown)
        )
        val listTitles = listOf(
            stringResource(id = R.string.base_happiness),
            stringResource(id = R.string.habitat),
            stringResource(id = R.string.grow_rate),
            stringResource(id = R.string.shape),
            stringResource(id = R.string.abilities)
        )
        Row(
            modifier = modifier
                .fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            OtherTitles(
                modifier = modifier,
                listTitles = listTitles,
                color = color,
                fontSize = fontSize,
                fontWeight = fontWTitle
            )
            OtherContents(
                modifier = modifier,
                listContents = listContent,
                color = color,
                fontSize = fontSize,
                fontWeight = fontWContent,
                abilities = abilities,
                language = language
            )
        }
    }
}

@Composable
fun OtherTitles(
    modifier: Modifier,
    listTitles: List<String>,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(0.4f)
    ) {
        listTitles.forEachIndexed { index, title ->
            Text(
                text = title,
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight
            )
            if (index < listTitles.size - 1) {
                Spacer(modifier = modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun OtherContents(
    modifier: Modifier,
    listContents: List<String>,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    abilities: List<AbilityTranslate?>?,
    language: String
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(0.65f)
    ) {
        listContents.forEachIndexed { index, content ->
            Text(
                text = content.ucFirst(),
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight
            )
            if (index < listContents.size) {
                Spacer(modifier = modifier.padding(4.dp))
            }
        }
        ListAbilities(
            modifier = modifier,
            abilities = abilities,
            color = color,
            fontSize = fontSize,
            language = language
        )
    }
}

@Composable
fun ListAbilities(
    modifier: Modifier,
    abilities: List<AbilityTranslate?>?,
    color: Color,
    fontSize: TextUnit,
    language: String
) {
    Row(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            var idAbility = 0
            abilities?.forEach {
                it?.names?.forEachIndexed { index, name ->
                    if (name.language.name == language) {
                        idAbility = index
                    }
                }
            }
            abilities?.forEach {
                if (it != null) {
                    Text(
                        text = it.names[idAbility].name.ucFirst(),
                        color = color,
                        fontSize = fontSize,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@Composable
fun HandleState(
    modifier: Modifier,
    state: PokedexPokemonInfoState,
    viewModel: PokedexPokemonViewModel,
    color: Color,
    fontSize: TextUnit,
    defaultLanguage: String
) {
    if (state.isLoading) {
        Box(
            contentAlignment = Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
    if (!state.isLoading && !state.isError) {
        PokemonDetails(
            modifier = modifier,
            viewModel = viewModel,
            color = color,
            fontSize = fontSize,
            language = defaultLanguage
        )
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
            Box(modifier = modifier.align(Alignment.CenterHorizontally)) {
                Button(
//                    onClick = { viewModel.onEvent(PokedexListEvent.OnReload(gen)) }
                    onClick = { Log.d("retry", "button") }
                ) {
                    Text(stringResource(id = R.string.retry))
                }
            }
        }
    }
}
package com.drimov.pokedex.presentation.pokedex_pokemon

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.drimov.pokedex.data.remote.dto.*
import com.drimov.pokedex.presentation.ui.theme.Blue700
import com.drimov.pokedex.util.UiEvent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.coroutineScope
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

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            when (viewModel.state.value.isLoading) {
                true -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                false -> {
                    val titles: List<String> = listOf("About", "Stats", "Moves")
                    HeaderInfo(color = Blue700, viewModel = viewModel)
                    CardContent(titles, pokemon = viewModel.state.value.pokemon!!)
//                    Text(text = viewModel.state.value.pokemon?.name ?: "")
//                    Text(text = viewModel.state.value.pokemon?.height.toString() ?: "")
                }
            }
        }
    }

}

@Composable
fun HeaderInfo(color: Color, viewModel: PokedexPokemonViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
    ) {
        val id = viewModel.state.value.pokemon?.id
        var text = "#"
        text += when (id) {
            in 0..9 -> "00${id}"
            in 10..99 -> "0${id}"
            else -> "$id"
        }
        Text(
            text = text ?: "",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .alpha(0.60f)
                .padding(5.dp),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )
        Text(
            text = viewModel.state.value.pokemon?.name?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
                ?: "",
            modifier = Modifier
                .padding(vertical = 26.dp)
                .padding(5.dp),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp
        )
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .componentRegistry {
                add(SvgDecoder(LocalContext.current))
            }
            .build()
        CompositionLocalProvider(LocalImageLoader provides imageLoader) {
            Image(
                painter = rememberImagePainter(
                    data = viewModel.state.value.url
                ),
                contentDescription = viewModel.state.value.pokemon?.name,
                modifier = Modifier
                    .size(200.dp)
                    .padding(32.dp)
                    .align(alignment = Alignment.TopCenter)
            )
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardContent(pages: List<String>, pokemon: Pokemon) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            Modifier.pagerTabIndicatorOffset(pagerState = pagerState, tabPositions = tabPositions)

        }
    ) {
        // Adds tabs for all of our pages
        pages.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                },
                text = {
                    Text(text = title)
                }
            )
        }

    }
    HorizontalPager(
        count = pages.size,
        state = pagerState
    ) { page ->
        when (page) {
            1 -> StatsCard(stats = pokemon.stats)
            2 -> MoveCard(forms = pokemon.moves)
        }
    }
}

@Composable
fun MoveCard(forms: List<MoveX>) {
    Card(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(count = 1, itemContent = {
                Text(
                    text = "Moves",
                    color = Blue700,
                    fontWeight = FontWeight.Medium
                )
                forms.forEach {
                    Text(text = it.move.name)

                }
            })
        }
    }
}

@Composable
fun StatsCard(stats: List<Stat>) {
    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            items(count = 1, itemContent = {
                Text(
                    text = "Base Stats",
                    color = Blue700,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(10.dp)
                )
                Row {
                    Column {
                        stats.forEach {
                            Text(
                                text = it.stat.name,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
                    Column {
                        stats.forEach {
                            Text(
                                text = it.base_stat.toString(),
                                modifier = Modifier
                                    .padding(10.dp)
                            )

                        }
                    }
                    Column {
                        stats.forEach {
                            val baseStat = it.base_stat/100.toFloat()
                            Log.d("base stat raw: ","${it.base_stat}")
                            Log.d("base stat calc: ","$baseStat")
                            Log.d("base stat: ","${it.base_stat.toFloat()}")
                            LinearProgressIndicator(
                                progress = baseStat,
                                modifier = Modifier
                                    .padding(19.dp)
                            )
                        }
                    }
                }

            })
        }
    }
}
/*
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        Text(text = it.stat.name)
                        Spacer(modifier = Modifier.padding(16.dp))
                        Text(text = it.base_stat.toString())
                        Spacer(modifier = Modifier.padding(16.dp))
                        Column {

                            val baseStat = it.base_stat / 10
                            LinearProgressIndicator(

                                progress = baseStat.toFloat(), Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(0.3f)
                            )
                        }
                    }
*
 */

@Composable
@Preview
fun PokedexPokemonPreview() {

}
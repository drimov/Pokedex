package com.drimov.pokedex.presentation.pokedex_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.drimov.pokedex.R
import com.drimov.pokedex.domain.model.PokedexListEntry
import com.drimov.pokedex.presentation.ui.theme.Grey200
import com.drimov.pokedex.util.parseNumberToIndex

@Composable
fun PokemonInfo(
    pokedexListEntry: PokedexListEntry,
    modifier: Modifier,
    viewModel: PokedexListViewModel,
    id: Int
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .clickable {
                viewModel.onEvent(
                    PokedexListEvent.OnPokemonClick(
                        id
                    )
                )
            },
        backgroundColor = Grey200
    ) {
        Row {
            ImagePokemon(entry = pokedexListEntry)
            TextPokemon(modifier = modifier, entry = pokedexListEntry)
        }
    }
}

@Composable
fun TextPokemon(modifier: Modifier,entry: PokedexListEntry){
    Column(
        modifier = modifier
    ) {
        Text(
            text = entry.name,
            modifier = modifier
                .padding(5.dp),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp
        )
        IdPokemon(modifier = modifier, id = entry.id)
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImagePokemon(entry: PokedexListEntry){
    Box {
        val painter = rememberImagePainter(
            data = entry.url,
            builder = {
                placeholder(R.drawable.ic_baseline_image_24)
                crossfade(500)
            }
        )
        val painterState = painter.state
        Image(
            painter = painter,
            contentDescription = entry.name,
            modifier = Modifier
                .size(150.dp)
                .padding(16.dp),
        )
        if (painterState is ImagePainter.State.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Center))
        }
    }
}

@Composable
fun IdPokemon(
    id: Int,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = parseNumberToIndex(id),
            modifier = modifier
                .align(BottomEnd)
                .padding(horizontal = 16.dp)
                .alpha(0.4f),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp,
            textAlign = TextAlign.Right
        )
    }
}
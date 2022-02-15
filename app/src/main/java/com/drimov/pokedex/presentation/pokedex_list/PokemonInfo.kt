package com.drimov.pokedex.presentation.pokedex_list

import android.util.Log
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
import coil.compose.ImagePainter

import coil.compose.rememberImagePainter
import com.drimov.pokedex.R

import com.drimov.pokedex.domain.model.PokedexListEntry
import com.drimov.pokedex.presentation.ui.theme.Grey200

@Composable
fun PokemonInfo(
    pokedexListEntry: PokedexListEntry,
    modifier: Modifier = Modifier,
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
        Row(
            modifier = Modifier
        ) {
            Box {
                val painter = rememberImagePainter(
                    data = pokedexListEntry.url,
                    builder = {
                        placeholder(R.drawable.ic_baseline_image_24)
                        crossfade(500)
                    }
                )
                val painterState = painter.state
                Image(
                    painter = painter,
                    contentDescription = pokedexListEntry.name,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(16.dp),
                )
                if (painterState is ImagePainter.State.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Center))
                }
            }

            Column(
                modifier = modifier
            ) {
                Text(
                    text = pokedexListEntry.name,
                    modifier = Modifier
                        .padding(5.dp),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp
                )
                IdPokemon(id = pokedexListEntry.id)
            }

        }

    }
}

@Composable
fun IdPokemon(id: Int) {
    var text = "#"
    text += when (id) {
        in 0..9 -> "00${id}"
        in 10..99 -> "0${id}"
        else -> "$id"
    }
    Log.d("test", "$text")
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = text,
            modifier = Modifier
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
package com.drimov.pokedex.presentation.pokedex_list

import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Constraints

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.ImageLoaderProvidableCompositionLocal
import coil.compose.LocalImageLoader

import coil.compose.rememberImagePainter
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.decode.SvgDecoder

import com.drimov.pokedex.domain.model.PokedexListEntry
import com.drimov.pokedex.presentation.ui.theme.Grey200
import com.drimov.pokedex.util.Constants
import javax.inject.Inject

@Composable
fun PokemonInfo(
    pokedexListEntry: PokedexListEntry,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(Grey200)
    ) {
        Row(
            modifier = Modifier
        ) {
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .componentRegistry {
                    add(SvgDecoder(LocalContext.current))
                }
                .build()
            CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                Image(
                    painter = rememberImagePainter(
                        data = pokedexListEntry.url
                    ),
                    contentDescription = pokedexListEntry.name,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(16.dp)
                )
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
//
//@Preview(showBackground = true)
//@Composable
//fun PokemonInfoPreview(@PreviewParameter(provider = FakePokedexEntry::class) fake: PokedexListEntry){
//    PokemonInfo(pokedexListEntry = fake, modifier = Modifier)
//}
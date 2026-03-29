package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

/** ポケモンの公式アートワークを横幅いっぱいの正方形領域に表示する画像。 */
@Composable
fun PokemonImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier =
            modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .aspectRatio(1f),
    )
}

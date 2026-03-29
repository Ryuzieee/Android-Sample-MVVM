package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** PokemonCard をグリッド表示するための共通データ。 */
data class PokemonGridItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
)

/** PokemonCard の2カラムグリッド表示。一覧・お気に入り画面で共通利用。 */
@Composable
fun PokemonGrid(
    items: List<PokemonGridItem>,
    onPokemonClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    AppLazyVerticalGrid(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(items, key = { it.id }) { item ->
            PokemonCard(
                name = item.name,
                id = item.id,
                imageUrl = item.imageUrl,
                onClick = { onPokemonClick(item.name) },
            )
        }
    }
}

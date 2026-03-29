package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

/** ポケモンの図鑑番号を `#id` 形式で表示するテキスト。 */
@Composable
fun PokemonIdText(
    id: Int,
    modifier: Modifier = Modifier,
) {
    AppText(
        text = "#$id",
        style = MaterialTheme.typography.bodySmall,
        secondary = true,
        modifier = modifier,
    )
}

/** ポケモンの名前を先頭大文字で表示するテキスト。スタイルは呼び出し元から指定する。 */
@Composable
fun PokemonNameText(
    name: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    AppText(
        text = name.replaceFirstChar { it.uppercase() },
        style = style,
        modifier = modifier,
    )
}

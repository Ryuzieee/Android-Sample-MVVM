package com.yamamuto.android_sample_mvvm.domain.model

private const val SPRITE_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"

/** ポケモンの一覧表示に必要な最小限のデータを表すドメインモデル。 */
data class PokemonSummaryModel(
    val name: String,
    val url: String,
) {
    val id: Int get() {
        return url.trimEnd('/').split('/').last().toInt()
    }
    val imageUrl: String get() {
        return "${SPRITE_URL}$id.png"
    }
}

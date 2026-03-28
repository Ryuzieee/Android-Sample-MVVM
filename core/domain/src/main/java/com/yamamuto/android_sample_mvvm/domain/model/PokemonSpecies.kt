package com.yamamuto.android_sample_mvvm.domain.model

/** ポケモン種族情報（図鑑テキスト・分類など）。 */
data class PokemonSpecies(
    val flavorText: String,
    val genus: String,
    val eggGroups: List<String>,
    val genderRate: Int,
    val captureRate: Int,
    val habitat: String?,
    val generation: String,
)

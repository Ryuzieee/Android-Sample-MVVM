package com.yamamuto.android_sample_mvvm.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val imageUrl: String,
    val stats: List<Stat>,
) {
    data class Stat(val name: String, val value: Int)
}

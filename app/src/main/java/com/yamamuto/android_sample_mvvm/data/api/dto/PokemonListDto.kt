package com.yamamuto.android_sample_mvvm.data.api.dto

data class PokemonListResponse(
    val results: List<PokemonDto>,
)

data class PokemonDto(
    val name: String,
    val url: String,
)

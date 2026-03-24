package com.yamamuto.android_sample_mvvm.data.api.dto

/**
 * ポケモン一覧APIレスポンスのDTO。
 *
 * ドメインモデルへのマッピングは [PokemonRepositoryImpl] で行う。
 */
data class PokemonListResponse(
    val results: List<PokemonDto>,
)

/** ポケモン一覧の各エントリDTO。 */
data class PokemonDto(
    val name: String,
    val url: String,
)

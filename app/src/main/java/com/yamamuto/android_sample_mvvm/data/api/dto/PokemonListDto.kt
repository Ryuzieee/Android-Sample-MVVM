package com.yamamuto.android_sample_mvvm.data.api.dto

import kotlinx.serialization.Serializable

/**
 * ポケモン一覧APIレスポンスのDTO。
 *
 * ドメインモデルへのマッピングは [PokemonRepositoryImpl] で行う。
 */
@Serializable
data class PokemonListResponse(
    val results: List<PokemonDto>,
)

/** ポケモン一覧の各エントリDTO。 */
@Serializable
data class PokemonDto(
    val name: String,
    val url: String,
)

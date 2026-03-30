package com.yamamuto.android_sample_mvvm.data.api.response

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

/**
 * ポケモン一覧APIレスポンスのDTO。
 *
 * ドメインモデルへのマッピングは [PokemonRepositoryImpl] で行う。
 */
@InternalSerializationApi
@Serializable
data class PokemonListResponse(
    val results: List<Item>,
) {
    /** ポケモン一覧の各エントリDTO。 */
    @Serializable
    data class Item(
        val name: String,
        val url: String,
    )
}

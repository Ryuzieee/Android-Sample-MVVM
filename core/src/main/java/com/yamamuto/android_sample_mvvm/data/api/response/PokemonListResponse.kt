package com.yamamuto.android_sample_mvvm.data.api.response

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

/** ポケモン一覧APIレスポンスのDTO。 */
@InternalSerializationApi
@Serializable
data class PokemonListResponse(
    val results: List<Item>,
) {
    @Serializable
    data class Item(
        val name: String,
        val url: String,
    )
}

package com.yamamuto.android_sample_mvvm.data.api.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

/** ability API レスポンスの DTO（日本語名取得用の最小構造）。 */
@InternalSerializationApi
@Serializable
data class AbilityResponse(
    val names: List<Name>,
) {
    @Serializable
    data class Name(
        val name: String,
        val language: PokemonSpeciesResponse.NamedResource,
    )
}

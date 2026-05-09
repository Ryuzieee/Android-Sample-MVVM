@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.network.mock

import com.yamamuto.android_sample_mvvm.data.api.response.AbilityResponse
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonSpeciesResponse.NamedResource
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json

internal fun buildMockAbilityResponse(
    json: Json,
    name: String,
): String {
    val jaName = MOCK_ABILITY_JA_NAMES[name] ?: name
    val response =
        AbilityResponse(
            names =
                listOf(
                    AbilityResponse.Name(name = jaName, language = NamedResource(name = "ja-Hrkt")),
                    AbilityResponse.Name(name = jaName, language = NamedResource(name = "ja")),
                    AbilityResponse.Name(name = name.replaceFirstChar { it.uppercase() }, language = NamedResource(name = "en")),
                ),
        )
    return json.encodeToString(response)
}

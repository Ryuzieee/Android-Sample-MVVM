@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.network.mock

import com.yamamuto.android_sample_mvvm.data.api.response.PokemonListResponse
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json

internal fun buildMockListResponse(json: Json): String {
    val response =
        PokemonListResponse(
            results =
                MOCK_POKEMONS.map { p ->
                    PokemonListResponse.Item(
                        name = p.name,
                        url = "https://pokeapi.co/api/v2/pokemon/${p.id}/",
                    )
                },
        )
    return json.encodeToString(response)
}

@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.network.mock

import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse.ChainLink
import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse.EvolutionDetail
import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse.Species
import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse.Trigger
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private fun species(
    name: String,
    id: Int,
) = Species(name = name, url = "https://pokeapi.co/api/v2/pokemon-species/$id/")

private fun levelUp(level: Int) = EvolutionDetail(minLevel = level, trigger = Trigger(name = "level-up"))

internal fun buildMockEvolutionResponse(json: Json): String {
    val response =
        EvolutionChainResponse(
            id = 1,
            chain =
                ChainLink(
                    species = species("bulbasaur", 1),
                    evolvesTo =
                        listOf(
                            ChainLink(
                                species = species("ivysaur", 2),
                                evolvesTo =
                                    listOf(
                                        ChainLink(
                                            species = species("venusaur", 3),
                                            evolvesTo = emptyList(),
                                            evolutionDetails = listOf(levelUp(32)),
                                        ),
                                    ),
                                evolutionDetails = listOf(levelUp(16)),
                            ),
                        ),
                ),
        )
    return json.encodeToString(response)
}

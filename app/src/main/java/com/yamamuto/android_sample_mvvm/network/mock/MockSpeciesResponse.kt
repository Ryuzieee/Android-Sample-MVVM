@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.network.mock

import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse.EvolutionChainRef
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse.FlavorTextEntry
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse.Genus
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse.Name
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse.NamedResource
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private fun lang(name: String) = NamedResource(name = name)

private val JA = lang("ja-Hrkt")
private val EN = lang("en")
private val SWORD = NamedResource(name = "sword")

internal fun buildMockSpeciesResponse(
    json: Json,
    name: String,
): String {
    val p = MOCK_POKEMON_BY_NAME[name] ?: MOCK_POKEMONS.first()

    val response =
        PokemonSpeciesResponse(
            names =
                listOf(
                    Name(name = p.jaName, language = JA),
                    Name(name = p.name.replaceFirstChar { it.uppercase() }, language = EN),
                ),
            flavorTextEntries =
                listOf(
                    FlavorTextEntry(flavorText = p.flavorText, language = JA, version = SWORD),
                    FlavorTextEntry(flavorText = "A mock English flavor text for ${p.name}.", language = EN, version = SWORD),
                ),
            evolutionChain = EvolutionChainRef(url = "https://pokeapi.co/api/v2/evolution-chain/${p.evolutionChainId}/"),
            genera =
                listOf(
                    Genus(genus = p.genus, language = JA),
                    Genus(genus = "Mock Pokémon", language = EN),
                ),
            eggGroups = p.eggGroups.map { NamedResource(name = it) },
            genderRate = p.genderRate,
            captureRate = p.captureRate,
            habitat = p.habitat?.let { NamedResource(name = it) },
            generation = NamedResource(name = p.generation),
        )
    return json.encodeToString(response)
}

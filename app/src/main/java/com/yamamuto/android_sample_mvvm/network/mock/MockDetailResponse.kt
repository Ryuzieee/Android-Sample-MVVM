@file:Suppress("ktlint:standard:max-line-length")
@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.network.mock

import com.yamamuto.android_sample_mvvm.data.api.response.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonDetailResponse.AbilityInfo
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonDetailResponse.AbilitySlot
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonDetailResponse.Sprites
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonDetailResponse.StatInfo
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonDetailResponse.StatSlot
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonDetailResponse.TypeInfo
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonDetailResponse.TypeSlot
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json

private val STAT_NAMES = listOf("hp", "attack", "defense", "special-attack", "special-defense", "speed")

internal fun buildMockDetailResponse(
    json: Json,
    name: String,
): String {
    val p = MOCK_POKEMON_BY_NAME[name] ?: MOCK_POKEMONS.first()
    val statValues = listOf(p.hp, p.attack, p.defense, p.spAttack, p.spDefense, p.speed)

    val response =
        PokemonDetailResponse(
            id = p.id,
            name = p.name,
            height = p.height,
            weight = p.weight,
            baseExperience = p.baseExperience,
            types = p.types.map { TypeSlot(type = TypeInfo(name = it)) },
            abilities =
                buildList {
                    p.abilities.forEachIndexed { i, a ->
                        add(AbilitySlot(ability = AbilityInfo(name = a), isHidden = false))
                    }
                    p.hiddenAbility?.let {
                        add(AbilitySlot(ability = AbilityInfo(name = it), isHidden = true))
                    }
                },
            sprites =
                Sprites(
                    other =
                        Sprites.Other(
                            officialArtwork =
                                Sprites.Other.OfficialArtwork(
                                    frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${p.id}.png",
                                ),
                        ),
                ),
            stats =
                STAT_NAMES.zip(statValues) { n, v ->
                    StatSlot(baseStat = v, stat = StatInfo(name = n))
                },
        )
    return json.encodeToString(response)
}

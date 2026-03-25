package com.yamamuto.android_sample_mvvm.util

import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDto
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonListResponse
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail

private const val OFFICIAL_ARTWORK_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"

/** テスト用のダミーデータをまとめたオブジェクト。 */
object TestFixtures {
    val fakePokemon =
        Pokemon(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon/1/",
        )

    val fakePokemonList = listOf(fakePokemon)

    val fakePokemonDetail =
        PokemonDetail(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            types = listOf("grass", "poison"),
            imageUrl = OFFICIAL_ARTWORK_URL,
            stats =
                listOf(
                    PokemonDetail.Stat(name = "hp", value = 45),
                    PokemonDetail.Stat(name = "attack", value = 49),
                ),
        )

    val fakePokemonListResponse =
        PokemonListResponse(
            results =
                listOf(
                    PokemonDto(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/1/",
                    ),
                ),
        )

    val fakePokemonDetailResponse =
        PokemonDetailResponse(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            types =
                listOf(
                    PokemonDetailResponse.TypeSlot(PokemonDetailResponse.TypeInfo("grass")),
                    PokemonDetailResponse.TypeSlot(PokemonDetailResponse.TypeInfo("poison")),
                ),
            sprites =
                PokemonDetailResponse.Sprites(
                    other =
                        PokemonDetailResponse.Sprites.Other(
                            officialArtwork =
                                PokemonDetailResponse.Sprites.Other.OfficialArtwork(
                                    frontDefault = OFFICIAL_ARTWORK_URL,
                                ),
                        ),
                ),
            stats =
                listOf(
                    PokemonDetailResponse.StatSlot(
                        baseStat = 45,
                        stat = PokemonDetailResponse.StatInfo("hp"),
                    ),
                    PokemonDetailResponse.StatSlot(
                        baseStat = 49,
                        stat = PokemonDetailResponse.StatInfo("attack"),
                    ),
                ),
        )
}

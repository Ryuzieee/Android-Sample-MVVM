package com.yamamuto.android_sample_mvvm.testing

import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel

private const val OFFICIAL_ARTWORK_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"

/** テスト用のダミーデータをまとめたオブジェクト。 */
object TestFixtures {
    val fakePokemon =
        PokemonSummaryModel(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon/1/",
        )

    val fakePokemonList = listOf(fakePokemon)

    val fakePokemonDetail =
        PokemonDetailModel(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            types = listOf("grass", "poison"),
            abilities = listOf(
                PokemonDetailModel.Ability(name = "overgrow", japaneseName = "しんりょく", isHidden = false),
                PokemonDetailModel.Ability(name = "chlorophyll", japaneseName = "ようりょくそ", isHidden = true),
            ),
            imageUrl = OFFICIAL_ARTWORK_URL,
            stats =
                listOf(
                    PokemonDetailModel.Stat(name = "hp", value = 45),
                    PokemonDetailModel.Stat(name = "attack", value = 49),
                ),
        )
}

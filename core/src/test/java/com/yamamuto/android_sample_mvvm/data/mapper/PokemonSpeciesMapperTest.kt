@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse
import kotlinx.serialization.InternalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PokemonSpeciesMapperTest {
    @Test
    fun `toModel extracts Japanese name, flavor text, and genus`() {
        val response =
            PokemonSpeciesResponse(
                names =
                    listOf(
                        PokemonSpeciesResponse.Name("Bulbasaur", PokemonSpeciesResponse.NamedResource("en")),
                        PokemonSpeciesResponse.Name("フシギダネ", PokemonSpeciesResponse.NamedResource("ja")),
                    ),
                flavorTextEntries =
                    listOf(
                        PokemonSpeciesResponse.FlavorTextEntry(
                            "A strange seed.",
                            PokemonSpeciesResponse.NamedResource("en"),
                            PokemonSpeciesResponse.NamedResource("red"),
                        ),
                        PokemonSpeciesResponse.FlavorTextEntry(
                            "たいようの ひかりを\nあびるほど からだに\u000cちからが わいてくる。",
                            PokemonSpeciesResponse.NamedResource("ja"),
                            PokemonSpeciesResponse.NamedResource("red"),
                        ),
                    ),
                evolutionChain = PokemonSpeciesResponse.EvolutionChainRef("https://pokeapi.co/api/v2/evolution-chain/1/"),
                genera =
                    listOf(
                        PokemonSpeciesResponse.Genus("Seed Pokémon", PokemonSpeciesResponse.NamedResource("en")),
                        PokemonSpeciesResponse.Genus("たねポケモン", PokemonSpeciesResponse.NamedResource("ja")),
                    ),
                eggGroups = listOf(PokemonSpeciesResponse.NamedResource("monster"), PokemonSpeciesResponse.NamedResource("plant")),
                genderRate = 1,
                captureRate = 45,
                habitat = PokemonSpeciesResponse.NamedResource("grassland"),
                generation = PokemonSpeciesResponse.NamedResource("generation-i"),
            )

        val model = response.toModel()

        assertEquals("フシギダネ", model.japaneseName)
        assertEquals("たいようの ひかりを あびるほど からだに ちからが わいてくる。", model.flavorText)
        assertEquals("たねポケモン", model.genus)
        assertEquals(listOf("monster", "plant"), model.eggGroups)
        assertEquals(1, model.genderRate)
        assertEquals(45, model.captureRate)
        assertEquals("grassland", model.habitat)
        assertEquals("generation-i", model.generation)
        assertEquals("https://pokeapi.co/api/v2/evolution-chain/1/", model.evolutionChainUrl)
    }

    @Test
    fun `toModel falls back to English when Japanese not available`() {
        val response =
            PokemonSpeciesResponse(
                names =
                    listOf(
                        PokemonSpeciesResponse.Name("Bulbasaur", PokemonSpeciesResponse.NamedResource("en")),
                    ),
                flavorTextEntries =
                    listOf(
                        PokemonSpeciesResponse.FlavorTextEntry(
                            "A strange seed.",
                            PokemonSpeciesResponse.NamedResource("en"),
                            PokemonSpeciesResponse.NamedResource("red"),
                        ),
                    ),
                evolutionChain = PokemonSpeciesResponse.EvolutionChainRef("https://pokeapi.co/api/v2/evolution-chain/1/"),
                genera =
                    listOf(
                        PokemonSpeciesResponse.Genus("Seed Pokémon", PokemonSpeciesResponse.NamedResource("en")),
                    ),
                eggGroups = emptyList(),
                genderRate = -1,
                captureRate = 45,
                habitat = null,
                generation = PokemonSpeciesResponse.NamedResource("generation-i"),
            )

        val model = response.toModel()

        assertEquals("", model.japaneseName)
        assertEquals("A strange seed.", model.flavorText)
        assertEquals("Seed Pokémon", model.genus)
        assertNull(model.habitat)
    }

    @Test
    fun `EvolutionChainResponse toModel flattens chain`() {
        val response =
            EvolutionChainResponse(
                id = 1,
                chain =
                    EvolutionChainResponse.ChainLink(
                        species = EvolutionChainResponse.Species("bulbasaur", "https://pokeapi.co/api/v2/pokemon-species/1/"),
                        evolvesTo =
                            listOf(
                                EvolutionChainResponse.ChainLink(
                                    species = EvolutionChainResponse.Species("ivysaur", "https://pokeapi.co/api/v2/pokemon-species/2/"),
                                    evolvesTo =
                                        listOf(
                                            EvolutionChainResponse.ChainLink(
                                                species =
                                                    EvolutionChainResponse.Species(
                                                        "venusaur",
                                                        "https://pokeapi.co/api/v2/pokemon-species/3/",
                                                    ),
                                                evolvesTo = emptyList(),
                                                evolutionDetails = listOf(EvolutionChainResponse.EvolutionDetail(minLevel = 32)),
                                            ),
                                        ),
                                    evolutionDetails = listOf(EvolutionChainResponse.EvolutionDetail(minLevel = 16)),
                                ),
                            ),
                        evolutionDetails = emptyList(),
                    ),
            )

        val stages = response.toModel()

        assertEquals(3, stages.size)
        assertEquals("bulbasaur", stages[0].name)
        assertEquals(1, stages[0].id)
        assertNull(stages[0].minLevel)
        assertEquals("ivysaur", stages[1].name)
        assertEquals(2, stages[1].id)
        assertEquals(16, stages[1].minLevel)
        assertEquals("venusaur", stages[2].name)
        assertEquals(3, stages[2].id)
        assertEquals(32, stages[2].minLevel)
    }

    @Test
    fun `extractIdFromUrl extracts id correctly`() {
        assertEquals(1, extractIdFromUrl("https://pokeapi.co/api/v2/pokemon-species/1/"))
        assertEquals(25, extractIdFromUrl("https://pokeapi.co/api/v2/pokemon-species/25/"))
        assertEquals(150, extractIdFromUrl("https://pokeapi.co/api/v2/pokemon-species/150"))
    }
}

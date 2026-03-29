@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.local.entity.AbilityEntry
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity
import com.yamamuto.android_sample_mvvm.data.local.entity.StatEntry
import kotlinx.serialization.InternalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonDetailMapperTest {
    private val response =
        PokemonDetailResponse(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            types =
                listOf(
                    PokemonDetailResponse.TypeSlot(PokemonDetailResponse.TypeInfo("grass")),
                    PokemonDetailResponse.TypeSlot(PokemonDetailResponse.TypeInfo("poison")),
                ),
            abilities =
                listOf(
                    PokemonDetailResponse.AbilitySlot(
                        ability = PokemonDetailResponse.AbilityInfo("overgrow"),
                        isHidden = false,
                    ),
                    PokemonDetailResponse.AbilitySlot(
                        ability = PokemonDetailResponse.AbilityInfo("chlorophyll"),
                        isHidden = true,
                    ),
                ),
            sprites =
                PokemonDetailResponse.Sprites(
                    other =
                        PokemonDetailResponse.Sprites.Other(
                            officialArtwork =
                                PokemonDetailResponse.Sprites.Other.OfficialArtwork(
                                    frontDefault = "https://example.com/1.png",
                                ),
                        ),
                ),
            stats =
                listOf(
                    PokemonDetailResponse.StatSlot(baseStat = 45, stat = PokemonDetailResponse.StatInfo("hp")),
                    PokemonDetailResponse.StatSlot(baseStat = 49, stat = PokemonDetailResponse.StatInfo("attack")),
                ),
        )

    @Test
    fun `DTOのフィールドをEntityに正しく変換する`() {
        val entity = response.toEntity()

        assertEquals(1, entity.id)
        assertEquals("bulbasaur", entity.name)
        assertEquals(7, entity.height)
        assertEquals(69, entity.weight)
        assertEquals(64, entity.baseExperience)
        assertEquals(listOf("grass", "poison"), entity.types)
        assertEquals("https://example.com/1.png", entity.imageUrl)
        assertEquals(2, entity.abilities.size)
        assertEquals(AbilityEntry(name = "overgrow", isHidden = false), entity.abilities[0])
        assertEquals(AbilityEntry(name = "chlorophyll", isHidden = true), entity.abilities[1])
        assertEquals(2, entity.stats.size)
        assertEquals(StatEntry(name = "hp", value = 45), entity.stats[0])
    }

    @Test
    fun `EntityのフィールドをModelに正しく変換する`() {
        val entity =
            PokemonDetailEntity(
                id = 25,
                name = "pikachu",
                height = 4,
                weight = 60,
                baseExperience = 112,
                types = listOf("electric"),
                abilities = listOf(AbilityEntry(name = "static", japaneseName = "せいでんき", isHidden = false)),
                imageUrl = "https://example.com/25.png",
                stats = listOf(StatEntry(name = "hp", value = 35)),
            )

        val model = entity.toModel()

        assertEquals(25, model.id)
        assertEquals("pikachu", model.name)
        assertEquals(4, model.height)
        assertEquals(60, model.weight)
        assertEquals(112, model.baseExperience)
        assertEquals(listOf("electric"), model.types)
        assertEquals("https://example.com/25.png", model.imageUrl)
        assertEquals(1, model.abilities.size)
        assertEquals("static", model.abilities[0].name)
        assertEquals("せいでんき", model.abilities[0].japaneseName)
        assertEquals(false, model.abilities[0].isHidden)
        assertEquals(1, model.stats.size)
        assertEquals("hp", model.stats[0].name)
        assertEquals(35, model.stats[0].value)
    }
}

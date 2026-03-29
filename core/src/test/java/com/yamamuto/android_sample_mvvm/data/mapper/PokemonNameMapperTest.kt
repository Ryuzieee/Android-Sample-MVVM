@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDto
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonListResponse
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonNameEntity
import kotlinx.serialization.InternalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonNameMapperTest {

    @Test
    fun `toEntity maps response results to entities`() {
        val response = PokemonListResponse(
            results = listOf(
                PokemonDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
                PokemonDto("charmander", "https://pokeapi.co/api/v2/pokemon/4/"),
            ),
        )

        val entities = response.toEntity()

        assertEquals(2, entities.size)
        assertEquals("bulbasaur", entities[0].name)
        assertEquals("charmander", entities[1].name)
    }

    @Test
    fun `toModel filters by query case-insensitively`() {
        val entities = listOf(
            PokemonNameEntity(name = "bulbasaur"),
            PokemonNameEntity(name = "charmander"),
            PokemonNameEntity(name = "charmeleon"),
            PokemonNameEntity(name = "pikachu"),
        )

        val result = entities.toModel("char")

        assertEquals(listOf("charmander", "charmeleon"), result)
    }

    @Test
    fun `toModel trims query whitespace`() {
        val entities = listOf(
            PokemonNameEntity(name = "pikachu"),
            PokemonNameEntity(name = "raichu"),
        )

        val result = entities.toModel("  pika  ")

        assertEquals(listOf("pikachu"), result)
    }

    @Test
    fun `toModel returns empty list when no match`() {
        val entities = listOf(PokemonNameEntity(name = "bulbasaur"))
        val result = entities.toModel("xyz")
        assertEquals(emptyList<String>(), result)
    }
}

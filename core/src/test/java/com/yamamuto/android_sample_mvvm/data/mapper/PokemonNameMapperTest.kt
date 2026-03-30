@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.api.response.PokemonListResponse
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonNameEntity
import kotlinx.serialization.InternalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonNameMapperTest {
    @Test
    fun `レスポンスのresultsをEntityリストに正しく変換する`() {
        val response =
            PokemonListResponse(
                results =
                    listOf(
                        PokemonListResponse.Item("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
                        PokemonListResponse.Item("charmander", "https://pokeapi.co/api/v2/pokemon/4/"),
                    ),
            )

        val entities = response.toEntity()

        assertEquals(2, entities.size)
        assertEquals("bulbasaur", entities[0].name)
        assertEquals("charmander", entities[1].name)
    }

    @Test
    fun `クエリで大文字小文字を区別せずフィルタリングする`() {
        val entities =
            listOf(
                PokemonNameEntity(name = "bulbasaur"),
                PokemonNameEntity(name = "charmander"),
                PokemonNameEntity(name = "charmeleon"),
                PokemonNameEntity(name = "pikachu"),
            )

        val result = entities.toModel("char")

        assertEquals(listOf("charmander", "charmeleon"), result)
    }

    @Test
    fun `クエリの前後の空白をトリムしてフィルタリングする`() {
        val entities =
            listOf(
                PokemonNameEntity(name = "pikachu"),
                PokemonNameEntity(name = "raichu"),
            )

        val result = entities.toModel("  pika  ")

        assertEquals(listOf("pikachu"), result)
    }

    @Test
    fun `一致するものがない場合に空リストを返す`() {
        val entities = listOf(PokemonNameEntity(name = "bulbasaur"))
        val result = entities.toModel("xyz")
        assertEquals(emptyList<String>(), result)
    }
}

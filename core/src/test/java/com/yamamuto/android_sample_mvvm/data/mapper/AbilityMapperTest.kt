@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.api.response.AbilityResponse
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonSpeciesResponse
import kotlinx.serialization.InternalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Test

class AbilityMapperTest {
    @Test
    fun `言語名マップを正しく生成する`() {
        val response =
            AbilityResponse(
                names =
                    listOf(
                        AbilityResponse.Name("Overgrow", PokemonSpeciesResponse.NamedResource("en")),
                        AbilityResponse.Name("しんりょく", PokemonSpeciesResponse.NamedResource("ja")),
                        AbilityResponse.Name("おおもり", PokemonSpeciesResponse.NamedResource("ja-Hrkt")),
                    ),
            )

        val result = response.toModel()

        assertEquals(3, result.size)
        assertEquals("Overgrow", result["en"])
        assertEquals("しんりょく", result["ja"])
    }

    @Test
    fun `namesが空の場合に空のマップを返す`() {
        val response = AbilityResponse(names = emptyList())
        val result = response.toModel()
        assertEquals(emptyMap<String, String>(), result)
    }
}

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteMapperTest {

    @Test
    fun `FavoriteEntity toModel maps correctly`() {
        val entity = FavoriteEntity(id = 25, name = "pikachu", imageUrl = "https://example.com/25.png")
        val model = entity.toModel()

        assertEquals(25, model.id)
        assertEquals("pikachu", model.name)
        assertEquals("https://example.com/25.png", model.imageUrl)
    }

    @Test
    fun `PokemonDetailModel toEntity maps correctly`() {
        val detail = PokemonDetailModel(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            types = listOf("grass"),
            abilities = emptyList(),
            imageUrl = "https://example.com/1.png",
            stats = emptyList(),
        )
        val entity = detail.toEntity()

        assertEquals(1, entity.id)
        assertEquals("bulbasaur", entity.name)
        assertEquals("https://example.com/1.png", entity.imageUrl)
    }
}

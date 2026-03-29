package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.local.dao.FavoriteDao
import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteRepositoryImplTest {

    private val dao = mockk<FavoriteDao>(relaxed = true)
    private val repository = FavoriteRepositoryImpl(dao)

    @Test
    fun `getFavorites returns mapped models`() = runTest {
        coEvery { dao.getAllFavorites() } returns listOf(
            FavoriteEntity(id = 1, name = "bulbasaur", imageUrl = "url1"),
            FavoriteEntity(id = 25, name = "pikachu", imageUrl = "url25"),
        )

        val result = repository.getFavorites()

        assertTrue(result.isSuccess)
        val favorites = result.getOrThrow()
        assertEquals(2, favorites.size)
        assertEquals("bulbasaur", favorites[0].name)
        assertEquals("pikachu", favorites[1].name)
    }

    @Test
    fun `getFavorites returns failure on exception`() = runTest {
        coEvery { dao.getAllFavorites() } throws RuntimeException("db error")

        val result = repository.getFavorites()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is AppException.Unknown)
    }

    @Test
    fun `isFavorite returns true when favorite exists`() = runTest {
        coEvery { dao.isFavorite(1) } returns true

        val result = repository.isFavorite(1)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrThrow())
    }

    @Test
    fun `isFavorite returns false when not favorite`() = runTest {
        coEvery { dao.isFavorite(1) } returns false

        val result = repository.isFavorite(1)

        assertTrue(result.isSuccess)
        assertEquals(false, result.getOrThrow())
    }

    @Test
    fun `addFavorite calls dao insertFavorite`() = runTest {
        val detail = PokemonDetailModel(
            id = 1, name = "bulbasaur", height = 7, weight = 69, baseExperience = 64,
            types = emptyList(), abilities = emptyList(), imageUrl = "url1", stats = emptyList(),
        )

        repository.addFavorite(detail)

        coVerify { dao.insertFavorite(match { it.id == 1 && it.name == "bulbasaur" }) }
    }

    @Test
    fun `removeFavorite calls dao deleteFavorite`() = runTest {
        repository.removeFavorite(1)

        coVerify { dao.deleteFavorite(1) }
    }
}

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.local.dao.FavoriteDao
import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteRepositoryImplTest {
    private val dao = mockk<FavoriteDao>()
    private val repository = FavoriteRepositoryImpl(dao)

    @Test
    fun `お気に入り一覧を正しく取得する`() =
        runTest {
            coEvery { dao.getAllFavorites() } returns
                listOf(
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
    fun `お気に入り取得時に例外が発生した場合にFailureを返す`() =
        runTest {
            coEvery { dao.getAllFavorites() } throws RuntimeException("db error")

            val result = repository.getFavorites()

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Unknown)
        }

    @Test
    fun `お気に入りが存在する場合にtrueを返す`() =
        runTest {
            coEvery { dao.isFavorite(1) } returns true

            val result = repository.isFavorite(1)

            assertTrue(result.isSuccess)
            assertEquals(true, result.getOrThrow())
        }

    @Test
    fun `お気に入りが存在しない場合にfalseを返す`() =
        runTest {
            coEvery { dao.isFavorite(1) } returns false

            val result = repository.isFavorite(1)

            assertTrue(result.isSuccess)
            assertEquals(false, result.getOrThrow())
        }

    @Test
    fun `お気に入り追加時にDAOのinsertFavoriteを呼び出す`() =
        runTest {
            coEvery { dao.insertFavorite(any()) } just Runs

            val detail =
                PokemonDetailModel(
                    id = 1,
                    name = "bulbasaur",
                    height = 7,
                    weight = 69,
                    baseExperience = 64,
                    types = emptyList(),
                    abilities = emptyList(),
                    imageUrl = "url1",
                    stats = emptyList(),
                )

            repository.addFavorite(detail)

            coVerify { dao.insertFavorite(match { it.id == 1 && it.name == "bulbasaur" }) }
        }

    @Test
    fun `お気に入り削除時にDAOのdeleteFavoriteを呼び出す`() =
        runTest {
            coEvery { dao.deleteFavorite(any()) } just Runs

            repository.removeFavorite(1)

            coVerify { dao.deleteFavorite(1) }
        }
}

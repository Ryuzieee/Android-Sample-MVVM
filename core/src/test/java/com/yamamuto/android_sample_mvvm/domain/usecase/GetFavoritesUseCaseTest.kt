package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetFavoritesUseCaseTest {
    private val repository = mockk<FavoriteRepository>()
    private val useCase = GetFavoritesUseCase(repository)

    @Test
    fun `returns favorites list on success`() =
        runTest {
            val favorites =
                listOf(
                    FavoriteModel(id = 1, name = "bulbasaur", imageUrl = "url1"),
                    FavoriteModel(id = 25, name = "pikachu", imageUrl = "url25"),
                )
            coEvery { repository.getFavorites() } returns Result.success(favorites)

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals(2, result.getOrThrow().size)
        }

    @Test
    fun `returns failure on error`() =
        runTest {
            coEvery { repository.getFavorites() } returns Result.failure(AppException.Unknown(Exception()))

            val result = useCase()

            assertTrue(result.isFailure)
        }
}

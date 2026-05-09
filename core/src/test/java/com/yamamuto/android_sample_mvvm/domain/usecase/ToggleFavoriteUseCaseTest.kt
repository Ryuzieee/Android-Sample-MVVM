package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class ToggleFavoriteUseCaseTest {
    private val repository = mockk<FavoriteRepository>()
    private val useCase = ToggleFavoriteUseCase(repository)

    private val detail =
        PokemonDetailModel(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            types = emptyList(),
            abilities = emptyList(),
            imageUrl = "url",
            stats = emptyList(),
        )

    @Test
    fun `お気に入り登録済みの場合に削除する`() =
        runTest {
            coEvery { repository.removeFavorite(any()) } returns Result.success(Unit)

            val result = useCase(detail, isFavorite = true)

            assertTrue(result.isSuccess)
            coVerify { repository.removeFavorite(1) }
            coVerify(exactly = 0) { repository.addFavorite(any()) }
        }

    @Test
    fun `お気に入り未登録の場合に追加する`() =
        runTest {
            coEvery { repository.addFavorite(any()) } returns Result.success(Unit)

            val result = useCase(detail, isFavorite = false)

            assertTrue(result.isSuccess)
            coVerify { repository.addFavorite(detail) }
            coVerify(exactly = 0) { repository.removeFavorite(any()) }
        }

    @Test
    fun `リポジトリの失敗をそのまま返す`() =
        runTest {
            coEvery { repository.addFavorite(any()) } returns Result.failure(AppException.Unknown(Exception("db error")))

            val result = useCase(detail, isFavorite = false)

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Unknown)
        }
}

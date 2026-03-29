package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchPokemonUseCaseTest {
    private val repository = mockk<PokemonRepository>()
    private val useCase = SearchPokemonUseCase(repository)

    @Test
    fun `検索成功時に一致する名前リストを返す`() =
        runTest {
            coEvery { repository.searchPokemonNames("pika") } returns Result.success(listOf("pikachu"))

            val result = useCase("pika")

            assertTrue(result.isSuccess)
            assertEquals(listOf("pikachu"), result.getOrThrow())
        }

    @Test
    fun `検索結果が空の場合にNotFoundエラーを返す`() =
        runTest {
            coEvery { repository.searchPokemonNames("xyz") } returns Result.success(emptyList())

            val result = useCase("xyz")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.NotFound)
        }

    @Test
    fun `リポジトリ失敗時にエラーを返す`() =
        runTest {
            coEvery { repository.searchPokemonNames(any()) } returns
                Result.failure(
                    AppException.Network(Exception()),
                )

            val result = useCase("pika")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Network)
        }
}

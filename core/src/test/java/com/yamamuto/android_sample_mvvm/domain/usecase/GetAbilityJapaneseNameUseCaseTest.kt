package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetAbilityJapaneseNameUseCaseTest {
    private val repository = mockk<PokemonRepository>()
    private val useCase = GetAbilityJapaneseNameUseCase(repository)

    @Test
    fun `jaキーが存在する場合に日本語名を返す`() =
        runTest {
            coEvery { repository.getAbilityLocalizedNames("overgrow") } returns
                Result.success(
                    mapOf("ja" to "しんりょく", "en" to "Overgrow"),
                )

            val result = useCase("overgrow")

            assertTrue(result.isSuccess)
            assertEquals("しんりょく", result.getOrThrow())
        }

    @Test
    fun `jaキーがない場合にja-hrktにフォールバックする`() =
        runTest {
            coEvery { repository.getAbilityLocalizedNames("overgrow") } returns
                Result.success(
                    mapOf("ja-hrkt" to "しんりょく", "en" to "Overgrow"),
                )

            val result = useCase("overgrow")

            assertTrue(result.isSuccess)
            assertEquals("しんりょく", result.getOrThrow())
        }

    @Test
    fun `日本語名がない場合に特性名にフォールバックする`() =
        runTest {
            coEvery { repository.getAbilityLocalizedNames("overgrow") } returns
                Result.success(
                    mapOf("en" to "Overgrow"),
                )

            val result = useCase("overgrow")

            assertTrue(result.isSuccess)
            assertEquals("overgrow", result.getOrThrow())
        }

    @Test
    fun `リポジトリ失敗時にエラーを返す`() =
        runTest {
            coEvery { repository.getAbilityLocalizedNames(any()) } returns
                Result.failure(
                    AppException.Network(Exception()),
                )

            val result = useCase("overgrow")

            assertTrue(result.isFailure)
        }
}

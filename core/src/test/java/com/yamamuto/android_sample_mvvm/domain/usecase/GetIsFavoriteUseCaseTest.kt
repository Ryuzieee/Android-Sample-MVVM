package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetIsFavoriteUseCaseTest {

    private val repository = mockk<FavoriteRepository>()
    private val useCase = GetIsFavoriteUseCase(repository)

    @Test
    fun `returns true when favorite exists`() = runTest {
        coEvery { repository.isFavorite(1) } returns Result.success(true)

        val result = useCase(1)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrThrow())
    }

    @Test
    fun `returns false when not favorite`() = runTest {
        coEvery { repository.isFavorite(1) } returns Result.success(false)

        val result = useCase(1)

        assertTrue(result.isSuccess)
        assertEquals(false, result.getOrThrow())
    }
}

package com.yamamuto.android_sample_mvvm.ui.favorites

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.usecase.GetFavoritesUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getFavoritesUseCase = mockk<GetFavoritesUseCase>()

    @Test
    fun `loads favorites on init and emits Success`() =
        runTest {
            val favorites =
                listOf(
                    FavoriteModel(id = 1, name = "bulbasaur", imageUrl = "url1"),
                    FavoriteModel(id = 25, name = "pikachu", imageUrl = "url25"),
                )
            coEvery { getFavoritesUseCase() } returns Result.success(favorites)

            val viewModel = FavoritesViewModel(getFavoritesUseCase)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state is UiState.Success)
            assertEquals(2, (state as UiState.Success).data.size)
        }

    @Test
    fun `emits Error when use case fails`() =
        runTest {
            coEvery { getFavoritesUseCase() } returns Result.failure(AppException.Unknown(Exception("db error")))

            val viewModel = FavoritesViewModel(getFavoritesUseCase)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value is UiState.Error)
        }

    @Test
    fun `emits Success with empty list when no favorites`() =
        runTest {
            coEvery { getFavoritesUseCase() } returns Result.success(emptyList())

            val viewModel = FavoritesViewModel(getFavoritesUseCase)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state is UiState.Success)
            assertEquals(0, (state as UiState.Success).data.size)
        }
}

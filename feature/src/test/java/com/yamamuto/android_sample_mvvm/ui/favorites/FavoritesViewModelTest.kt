package com.yamamuto.android_sample_mvvm.ui.favorites

import app.cash.turbine.test
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.usecase.GetFavoritesUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getFavoritesUseCase = mockk<GetFavoritesUseCase>()

    private fun createViewModel() = FavoritesViewModel(getFavoritesUseCase)

    @Test
    fun `お気に入り取得成功時にSuccess状態になる`() =
        runTest {
            val favorites =
                listOf(
                    FavoriteModel(id = 1, name = "bulbasaur", imageUrl = "url1"),
                    FavoriteModel(id = 25, name = "pikachu", imageUrl = "url25"),
                )
            coEvery { getFavoritesUseCase() } returns Result.success(favorites)

            val viewModel = createViewModel()

            viewModel.uiState.test {
                val state = awaitItem().content
                assertTrue(state is UiState.Success)
                assertEquals(2, (state as UiState.Success).data.size)
            }
        }

    @Test
    fun `お気に入り取得失敗時にError状態になる`() =
        runTest {
            coEvery { getFavoritesUseCase() } returns Result.failure(AppException.Unknown(Exception("db error")))

            val viewModel = createViewModel()

            viewModel.uiState.test {
                assertTrue(awaitItem().content is UiState.Error)
            }
        }

    @Test
    fun `お気に入りが空の場合は空リストでSuccess状態になる`() =
        runTest {
            coEvery { getFavoritesUseCase() } returns Result.success(emptyList())

            val viewModel = createViewModel()

            viewModel.uiState.test {
                val state = awaitItem().content
                assertTrue(state is UiState.Success)
                assertEquals(0, (state as UiState.Success).data.size)
            }
        }
}

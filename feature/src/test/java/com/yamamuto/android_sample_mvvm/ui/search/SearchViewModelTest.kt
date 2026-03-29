package com.yamamuto.android_sample_mvvm.ui.search

import app.cash.turbine.test
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.usecase.SearchPokemonUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchPokemonUseCase = mockk<SearchPokemonUseCase>()

    private fun createViewModel() = SearchViewModel(searchPokemonUseCase)

    @Test
    fun `初期状態はクエリが空でIdle状態になる`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals("", state.query)
                assertEquals(UiState.Idle, state.content)
            }
        }

    @Test
    fun `クエリ変更時にクエリが更新される`() =
        runTest {
            coEvery { searchPokemonUseCase(any()) } returns Result.success(emptyList())
            val viewModel = createViewModel()

            viewModel.uiState.test {
                awaitItem() // Initial
                viewModel.onQueryChange("pika")
                assertEquals("pika", awaitItem().query)
            }
        }

    @Test
    fun `デバウンス後に検索成功でSuccess状態になる`() =
        runTest {
            coEvery { searchPokemonUseCase("pikachu") } returns Result.success(listOf("pikachu"))
            val viewModel = createViewModel()

            viewModel.uiState.test {
                awaitItem() // Initial idle
                viewModel.onQueryChange("pikachu")
                awaitItem() // Query updated

                advanceTimeBy(600)
                val state = expectMostRecentItem()
                assertTrue(state.content is UiState.Success)
                assertEquals(listOf("pikachu"), (state.content as UiState.Success).data)
            }
        }

    @Test
    fun `空クエリでIdle状態にリセットされる`() =
        runTest {
            coEvery { searchPokemonUseCase("pika") } returns Result.success(listOf("pikachu"))
            val viewModel = createViewModel()

            viewModel.uiState.test {
                awaitItem() // Initial
                viewModel.onQueryChange("pika")
                awaitItem() // Query updated
                advanceTimeBy(600)
                expectMostRecentItem() // Success after search

                viewModel.onQueryChange("")
                advanceTimeBy(600)
                assertEquals(UiState.Idle, expectMostRecentItem().content)
            }
        }

    @Test
    fun `検索失敗時にError状態になる`() =
        runTest {
            coEvery { searchPokemonUseCase("xyz") } returns Result.failure(AppException.NotFound("xyz"))
            val viewModel = createViewModel()

            viewModel.uiState.test {
                awaitItem() // Initial
                viewModel.onQueryChange("xyz")
                awaitItem() // Query updated
                advanceTimeBy(600)
                assertTrue(expectMostRecentItem().content is UiState.Error)
            }
        }

    @Test
    fun `retrySearchでデバウンスなしに再検索できる`() =
        runTest {
            coEvery { searchPokemonUseCase("pika") } returns Result.failure(AppException.Network(Exception()))
            val viewModel = createViewModel()

            viewModel.uiState.test {
                awaitItem() // Initial
                viewModel.onQueryChange("pika")
                awaitItem() // Query updated
                advanceTimeBy(600)
                assertTrue(expectMostRecentItem().content is UiState.Error)

                coEvery { searchPokemonUseCase("pika") } returns Result.success(listOf("pikachu"))
                viewModel.retrySearch()

                assertTrue(expectMostRecentItem().content is UiState.Success)
            }
        }

    @Test
    fun `クエリが空の場合retrySearchは何もしない`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.uiState.test {
                awaitItem() // Initial idle
                viewModel.retrySearch()
                expectNoEvents()
            }
        }
}

package com.yamamuto.android_sample_mvvm.ui.search

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.usecase.SearchPokemonUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
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
    fun `初期状態はクエリが空でIdle状態になる`() {
        val viewModel = createViewModel()

        assertEquals("", viewModel.uiState.value.query)
        assertEquals(UiState.Idle, viewModel.uiState.value.content)
    }

    @Test
    fun `クエリ変更時にクエリが更新される`() {
        val viewModel = createViewModel()

        viewModel.onQueryChange("pika")

        assertEquals("pika", viewModel.uiState.value.query)
    }

    @Test
    fun `デバウンス後に検索成功でSuccess状態になる`() =
        runTest {
            coEvery { searchPokemonUseCase("pikachu") } returns Result.success(listOf("pikachu"))
            val viewModel = createViewModel()

            viewModel.onQueryChange("pikachu")
            advanceTimeBy(600)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state.content is UiState.Success)
            assertEquals(listOf("pikachu"), (state.content as UiState.Success).data)
        }

    @Test
    fun `空クエリでIdle状態にリセットされる`() =
        runTest {
            coEvery { searchPokemonUseCase("pika") } returns Result.success(listOf("pikachu"))
            val viewModel = createViewModel()

            viewModel.onQueryChange("pika")
            advanceTimeBy(600)
            advanceUntilIdle()

            viewModel.onQueryChange("")
            advanceTimeBy(600)
            advanceUntilIdle()

            assertEquals(UiState.Idle, viewModel.uiState.value.content)
        }

    @Test
    fun `検索失敗時にError状態になる`() =
        runTest {
            coEvery { searchPokemonUseCase("xyz") } returns Result.failure(AppException.NotFound("xyz"))
            val viewModel = createViewModel()

            viewModel.onQueryChange("xyz")
            advanceTimeBy(600)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.content is UiState.Error)
        }

    @Test
    fun `retrySearchでデバウンスなしに再検索できる`() =
        runTest {
            coEvery { searchPokemonUseCase("pika") } returns Result.failure(AppException.Network(Exception()))
            val viewModel = createViewModel()

            viewModel.onQueryChange("pika")
            advanceTimeBy(600)
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.content is UiState.Error)

            coEvery { searchPokemonUseCase("pika") } returns Result.success(listOf("pikachu"))
            viewModel.retrySearch()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.content is UiState.Success)
        }

    @Test
    fun `クエリが空の場合retrySearchは何もしない`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.retrySearch()
            advanceUntilIdle()

            assertEquals(UiState.Idle, viewModel.uiState.value.content)
        }
}

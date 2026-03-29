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
    fun `initial state is idle with empty query`() {
        val viewModel = createViewModel()

        assertEquals("", viewModel.uiState.value.query)
        assertEquals(UiState.Idle, viewModel.uiState.value.content)
    }

    @Test
    fun `onQueryChange updates query`() {
        val viewModel = createViewModel()

        viewModel.onQueryChange("pika")

        assertEquals("pika", viewModel.uiState.value.query)
    }

    @Test
    fun `search returns success after debounce`() = runTest {
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
    fun `blank query resets to idle`() = runTest {
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
    fun `search returns error state on failure`() = runTest {
        coEvery { searchPokemonUseCase("xyz") } returns Result.failure(AppException.NotFound("xyz"))
        val viewModel = createViewModel()

        viewModel.onQueryChange("xyz")
        advanceTimeBy(600)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.content is UiState.Error)
    }

    @Test
    fun `retrySearch executes without debounce`() = runTest {
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
    fun `retrySearch does nothing when query is blank`() = runTest {
        val viewModel = createViewModel()

        viewModel.retrySearch()
        advanceUntilIdle()

        assertEquals(UiState.Idle, viewModel.uiState.value.content)
    }
}

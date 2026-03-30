package com.yamamuto.android_sample_mvvm.ui.list

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.testing.TestFixtures.fakePokemonList
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

private const val PAGE_SIZE = 20

class PokemonListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPokemonList = mockk<GetPokemonListUseCase>()

    private fun createViewModel(): PokemonListViewModel {
        return PokemonListViewModel(getPokemonList)
    }

    @Test
    fun `初回読み込みでポケモン一覧を取得できる`() =
        runTest {
            coEvery { getPokemonList(offset = 0, limit = any()) } returns Result.success(fakePokemonList)

            val viewModel = createViewModel()

            assertTrue(viewModel.uiState.value.loadState is UiState.Success)
            assertEquals(fakePokemonList, viewModel.uiState.value.items)
        }

    @Test
    fun `空リストの場合はitemsが空になる`() =
        runTest {
            coEvery { getPokemonList(offset = 0, limit = any()) } returns Result.success(emptyList())

            val viewModel = createViewModel()

            val state = viewModel.uiState.value
            assertTrue(state.loadState is UiState.Success)
            assertTrue(state.items.isEmpty())
            assertFalse(state.hasMore)
        }

    @Test
    fun `初回読み込み失敗でエラー状態になる`() =
        runTest {
            coEvery { getPokemonList(offset = 0, limit = any()) } returns
                Result.failure(AppException.Network(Exception("network error")))

            val viewModel = createViewModel()
            val state = viewModel.uiState.value

            assertTrue(state.loadState is UiState.Error)
            assertTrue(state.items.isEmpty())
        }

    @Test
    fun `loadMoreで追加データが既存リストに追加される`() =
        runTest {
            val fullPage =
                List(PAGE_SIZE) {
                    PokemonSummaryModel(
                        name = "pokemon-$it",
                        url = "https://pokeapi.co/api/v2/pokemon/${it + 1}/",
                    )
                }
            coEvery { getPokemonList(offset = 0, limit = PAGE_SIZE) } returns Result.success(fullPage)
            coEvery { getPokemonList(offset = PAGE_SIZE, limit = PAGE_SIZE) } returns
                Result.success(fakePokemonList)

            val viewModel = createViewModel()
            viewModel.loadMore()

            assertEquals(fullPage.size + fakePokemonList.size, viewModel.uiState.value.items.size)
        }

    @Test
    fun `refreshでリストがリセットされる`() =
        runTest {
            coEvery { getPokemonList(offset = 0, limit = any()) } returns Result.success(fakePokemonList)

            val viewModel = createViewModel()
            viewModel.refresh()

            assertEquals(fakePokemonList, viewModel.uiState.value.items)
            assertFalse(viewModel.uiState.value.isRefreshing)
        }
}

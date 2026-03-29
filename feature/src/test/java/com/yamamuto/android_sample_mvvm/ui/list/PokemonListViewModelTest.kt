package com.yamamuto.android_sample_mvvm.ui.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.yamamuto.android_sample_mvvm.data.paging.PagingSourceFactory
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.testing.TestFixtures.fakePokemonList
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PokemonListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val pagingSourceFactory = mockk<PagingSourceFactory<PokemonSummaryModel>>()

    private fun fakePagingSource(items: List<PokemonSummaryModel>): PagingSource<Int, PokemonSummaryModel> {
        return object : PagingSource<Int, PokemonSummaryModel>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonSummaryModel> {
                return LoadResult.Page(data = items, prevKey = null, nextKey = null)
            }

            override fun getRefreshKey(state: PagingState<Int, PokemonSummaryModel>): Int? {
                return null
            }
        }
    }

    private fun createViewModel(): PokemonListViewModel {
        return PokemonListViewModel(pagingSourceFactory)
    }

    @Test
    fun `Pagingでポケモン一覧を取得できる`() =
        runTest {
            every { pagingSourceFactory.create() } returns fakePagingSource(fakePokemonList)

            val viewModel = createViewModel()

            val pagingData = viewModel.uiState.value.pagingData
            val items = flowOf(pagingData).asSnapshot()
            assertEquals(fakePokemonList.size, items.size)
            assertEquals("bulbasaur", items[0].name)
        }

    @Test
    fun `空リストの場合は空のPagingDataになる`() =
        runTest {
            every { pagingSourceFactory.create() } returns fakePagingSource(emptyList())

            val viewModel = createViewModel()

            val pagingData = viewModel.uiState.value.pagingData
            val items = flowOf(pagingData).asSnapshot()
            assertEquals(0, items.size)
        }
}

package com.yamamuto.android_sample_mvvm.ui.list

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.yamamuto.android_sample_mvvm.data.paging.PokemonPagingSourceFactory
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.testing.TestFixtures.fakePokemonList
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * [PokemonListViewModel] の単体テスト。
 *
 * Paging のデータ取得をテストする。
 */
class PokemonListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var pagingSourceFactory: PokemonPagingSourceFactory

    @Before
    fun setUp() {
        pagingSourceFactory = mockk()
    }

    private fun fakePagingSource(items: List<Pokemon>): PagingSource<Int, Pokemon> {
        return object : PagingSource<Int, Pokemon>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
                return LoadResult.Page(data = items, prevKey = null, nextKey = null)
            }

            override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
                return null
            }
        }
    }

    @Test
    fun `Paging でポケモン一覧を取得できる`() {
        runTest {
            every { pagingSourceFactory.create() } returns fakePagingSource(fakePokemonList)

            val viewModel = PokemonListViewModel(pagingSourceFactory)

            viewModel.uiState.test {
                val pagingData = awaitItem().pagingData
                val items = flowOf(pagingData).asSnapshot()
                assertEquals(fakePokemonList.size, items.size)
                assertEquals("bulbasaur", items[0].name)
            }
        }
    }

    @Test
    fun `空リストの場合は空のPagingDataになる`() {
        runTest {
            every { pagingSourceFactory.create() } returns fakePagingSource(emptyList())

            val viewModel = PokemonListViewModel(pagingSourceFactory)

            viewModel.uiState.test {
                val pagingData = awaitItem().pagingData
                val items = flowOf(pagingData).asSnapshot()
                assertEquals(0, items.size)
            }
        }
    }
}

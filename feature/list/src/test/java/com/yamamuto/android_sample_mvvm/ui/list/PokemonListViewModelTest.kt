package com.yamamuto.android_sample_mvvm.ui.list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.testing.TestFixtures.fakePokemonList
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * [PokemonListViewModel] の単体テスト。
 *
 * Paging のデータ取得をテストする。
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class PokemonListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var useCase: GetPokemonListUseCase

    @Before
    fun setUp() {
        useCase = mockk()
    }

    private fun pagingFlowOf(data: List<Pokemon>) =
        Pager(PagingConfig(pageSize = 20)) {
            object : PagingSource<Int, Pokemon>() {
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> =
                    LoadResult.Page(data = data, prevKey = null, nextKey = null)

                override fun getRefreshKey(state: PagingState<Int, Pokemon>) = null
            }
        }.flow

    @Test
    fun `Paging でポケモン一覧を取得できる`() =
        runTest(mainDispatcherRule.testDispatcher) {
            every { useCase() } returns pagingFlowOf(fakePokemonList)

            val viewModel = PokemonListViewModel(useCase)

            val items = viewModel.uiState.value.pagingData.asSnapshot()
            assertEquals(fakePokemonList.size, items.size)
            assertEquals("bulbasaur", items[0].name)
        }

    @Test
    fun `空リストの場合は空のPagingDataになる`() =
        runTest(mainDispatcherRule.testDispatcher) {
            every { useCase() } returns pagingFlowOf(emptyList())

            val viewModel = PokemonListViewModel(useCase)

            val items = viewModel.uiState.value.pagingData.asSnapshot()
            assertEquals(0, items.size)
        }
}

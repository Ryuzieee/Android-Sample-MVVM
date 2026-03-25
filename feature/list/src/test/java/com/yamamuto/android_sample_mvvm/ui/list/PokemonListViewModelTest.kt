package com.yamamuto.android_sample_mvvm.ui.list

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import com.yamamuto.android_sample_mvvm.util.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonList
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

    private lateinit var useCase: GetPokemonListUseCase

    @Before
    fun setUp() {
        useCase = mockk()
    }

    @Test
    fun `Paging でポケモン一覧を取得できる`() =
        runTest {
            every { useCase() } returns flowOf(PagingData.from(fakePokemonList))

            val viewModel = PokemonListViewModel(useCase)

            val items = viewModel.pokemonPagingFlow.asSnapshot()
            assertEquals(fakePokemonList.size, items.size)
            assertEquals("bulbasaur", items[0].name)
        }

    @Test
    fun `空リストの場合は空のPagingDataになる`() =
        runTest {
            every { useCase() } returns flowOf(PagingData.from(emptyList()))

            val viewModel = PokemonListViewModel(useCase)

            val items = viewModel.pokemonPagingFlow.asSnapshot()
            assertEquals(0, items.size)
        }
}

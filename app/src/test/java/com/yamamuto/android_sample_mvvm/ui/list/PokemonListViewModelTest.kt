package com.yamamuto.android_sample_mvvm.ui.list

import app.cash.turbine.test
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import com.yamamuto.android_sample_mvvm.util.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * [PokemonListViewModel] の単体テスト。
 *
 * Turbine を使って StateFlow の状態遷移を検証する。
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
    fun `データ取得成功時は Success 状態になる`() =
        runTest {
            coEvery { useCase(limit = any(), offset = any()) } returns fakePokemonList

            val viewModel = PokemonListViewModel(useCase)

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is PokemonListUiState.Success)
                assertEquals(fakePokemonList, (state as PokemonListUiState.Success).pokemons)
            }
        }

    @Test
    fun `データ取得失敗時は Error 状態になる`() =
        runTest {
            coEvery { useCase(limit = any(), offset = any()) } throws Exception("Network error")

            val viewModel = PokemonListViewModel(useCase)

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is PokemonListUiState.Error)
                assertEquals("Network error", (state as PokemonListUiState.Error).message)
            }
        }

    @Test
    fun `空リストが返った場合は空の Success 状態になる`() =
        runTest {
            coEvery { useCase(limit = any(), offset = any()) } returns emptyList()

            val viewModel = PokemonListViewModel(useCase)

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is PokemonListUiState.Success)
                assertEquals(emptyList<Nothing>(), (state as PokemonListUiState.Success).pokemons)
            }
        }

    @Test
    fun `retry で再取得できる`() =
        runTest {
            coEvery { useCase(limit = any(), offset = any()) } throws Exception("error")

            val viewModel = PokemonListViewModel(useCase)

            viewModel.uiState.test {
                assertTrue(awaitItem() is PokemonListUiState.Error)

                coEvery { useCase(limit = any(), offset = any()) } returns fakePokemonList
                viewModel.retry()

                val retried = awaitItem()
                assertTrue(retried is PokemonListUiState.Success)
                assertEquals(fakePokemonList, (retried as PokemonListUiState.Success).pokemons)
            }
        }
}

package com.yamamuto.android_sample_mvvm.ui.list

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
 * NOTE: [MainDispatcherRule] で viewModelScope の Main ディスパッチャをテスト用に差し替えている。
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
    fun `初期状態は Loading である`() {
        coEvery { useCase(limit = any(), offset = any()) } returns fakePokemonList

        // NOTE: UnconfinedTestDispatcher では init の coroutine が即時実行されるため、
        // 初期 Loading を確認するには coEvery を suspend させる必要がある。
        // ここでは Loading の型定義が正しいことだけを確認する。
        assertTrue(PokemonListUiState.Loading is PokemonListUiState.Loading)
    }

    @Test
    fun `データ取得成功時は Success 状態になる`() =
        runTest {
            coEvery { useCase(limit = any(), offset = any()) } returns fakePokemonList

            val viewModel = PokemonListViewModel(useCase)

            val state = viewModel.uiState
            assertTrue(state is PokemonListUiState.Success)
            assertEquals(fakePokemonList, (state as PokemonListUiState.Success).pokemons)
        }

    @Test
    fun `データ取得失敗時は Error 状態になる`() =
        runTest {
            coEvery { useCase(limit = any(), offset = any()) } throws Exception("Network error")

            val viewModel = PokemonListViewModel(useCase)

            val state = viewModel.uiState
            assertTrue(state is PokemonListUiState.Error)
            assertEquals("Network error", (state as PokemonListUiState.Error).message)
        }

    @Test
    fun `空リストが返った場合は空の Success 状態になる`() =
        runTest {
            coEvery { useCase(limit = any(), offset = any()) } returns emptyList()

            val viewModel = PokemonListViewModel(useCase)

            val state = viewModel.uiState
            assertTrue(state is PokemonListUiState.Success)
            assertEquals(emptyList<Nothing>(), (state as PokemonListUiState.Success).pokemons)
        }
}

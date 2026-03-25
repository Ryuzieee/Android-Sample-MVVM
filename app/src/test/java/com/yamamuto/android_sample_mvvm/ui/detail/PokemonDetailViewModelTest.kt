package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import com.yamamuto.android_sample_mvvm.util.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonDetail
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * [PokemonDetailViewModel] の単体テスト。
 *
 * Turbine を使って StateFlow の状態遷移を検証する。
 */
class PokemonDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var useCase: GetPokemonDetailUseCase

    @Before
    fun setUp() {
        useCase = mockk()
    }

    private fun createViewModel(pokemonName: String = "bulbasaur"): PokemonDetailViewModel =
        PokemonDetailViewModel(
            getPokemonDetailUseCase = useCase,
            savedStateHandle = SavedStateHandle(mapOf("name" to pokemonName)),
        )

    @Test
    fun `データ取得成功時は Success 状態になる`() =
        runTest {
            coEvery { useCase("bulbasaur") } returns fakePokemonDetail

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is PokemonDetailUiState.Success)
                assertEquals(fakePokemonDetail, (state as PokemonDetailUiState.Success).detail)
            }
        }

    @Test
    fun `データ取得失敗時は Error 状態になる`() =
        runTest {
            coEvery { useCase("bulbasaur") } throws Exception("Not found")

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is PokemonDetailUiState.Error)
                assertEquals("Not found", (state as PokemonDetailUiState.Error).message)
            }
        }

    @Test
    fun `異なるポケモン名で正しくデータを取得する`() =
        runTest {
            val charizardDetail = fakePokemonDetail.copy(id = 6, name = "charizard")
            coEvery { useCase("charizard") } returns charizardDetail

            val viewModel = createViewModel("charizard")

            viewModel.uiState.test {
                val state = awaitItem() as PokemonDetailUiState.Success
                assertEquals("charizard", state.detail.name)
                assertEquals(6, state.detail.id)
            }
        }

    @Test
    fun `retry で再取得できる`() =
        runTest {
            coEvery { useCase("bulbasaur") } throws Exception("error")

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                assertTrue(awaitItem() is PokemonDetailUiState.Error)

                coEvery { useCase("bulbasaur") } returns fakePokemonDetail
                viewModel.retry()

                val retried = awaitItem()
                assertTrue(retried is PokemonDetailUiState.Success)
            }
        }
}

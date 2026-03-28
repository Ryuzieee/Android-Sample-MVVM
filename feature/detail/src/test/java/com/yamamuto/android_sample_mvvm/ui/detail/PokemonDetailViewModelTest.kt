package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.testing.TestFixtures.fakePokemonDetail
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var getIsFavoriteUseCase: GetIsFavoriteUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @Before
    fun setUp() {
        useCase = mockk()
        getIsFavoriteUseCase = mockk()
        toggleFavoriteUseCase = mockk()
        every { getIsFavoriteUseCase(any()) } returns flowOf(false)
        coEvery { toggleFavoriteUseCase(any(), any()) } just Runs
    }

    private fun createViewModel(pokemonName: String = "bulbasaur"): PokemonDetailViewModel =
        PokemonDetailViewModel(
            getPokemonDetailUseCase = useCase,
            getIsFavoriteUseCase = getIsFavoriteUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            savedStateHandle = SavedStateHandle(mapOf("name" to pokemonName)),
        )

    @Test
    fun `データ取得成功時は Success 状態になる`() =
        runTest {
            coEvery { useCase("bulbasaur") } returns fakePokemonDetail

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state.contentState is UiState.Success)
                assertEquals(fakePokemonDetail, (state.contentState as UiState.Success).data)
            }
        }

    @Test
    fun `データ取得失敗時は Error 状態になる`() =
        runTest {
            coEvery { useCase("bulbasaur") } throws Exception("Not found")

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state.contentState is UiState.Error)
                assertEquals("Not found", (state.contentState as UiState.Error).message)
            }
        }

    @Test
    fun `異なるポケモン名で正しくデータを取得する`() =
        runTest {
            val charizardDetail = fakePokemonDetail.copy(id = 6, name = "charizard")
            coEvery { useCase("charizard") } returns charizardDetail

            val viewModel = createViewModel("charizard")

            viewModel.uiState.test {
                val state = awaitItem().contentState as UiState.Success
                assertEquals("charizard", state.data.name)
                assertEquals(6, state.data.id)
            }
        }

    @Test
    fun `retry で再取得できる`() =
        runTest {
            coEvery { useCase("bulbasaur") } throws Exception("error")

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                assertTrue(awaitItem().contentState is UiState.Error)

                coEvery { useCase("bulbasaur") } returns fakePokemonDetail
                viewModel.retry()

                assertTrue(awaitItem().contentState is UiState.Success)
            }
        }
}

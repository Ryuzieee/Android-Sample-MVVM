package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonFullDetailModel
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonFullDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.testing.TestFixtures.fakePokemonDetail
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
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

    private lateinit var getPokemonFullDetailUseCase: GetPokemonFullDetailUseCase
    private lateinit var getIsFavoriteUseCase: GetIsFavoriteUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    private val fakeFullDetail =
        PokemonFullDetailModel(
            detail = fakePokemonDetail,
            species = null,
            evolutionChain = emptyList(),
        )

    @Before
    fun setUp() {
        getPokemonFullDetailUseCase = mockk()
        getIsFavoriteUseCase = mockk()
        toggleFavoriteUseCase = mockk()
        coEvery { getIsFavoriteUseCase(any()) } returns Result.success(false)
        coEvery { toggleFavoriteUseCase(any(), any()) } just Runs
    }

    private fun createViewModel(pokemonName: String = "bulbasaur"): PokemonDetailViewModel {
        return PokemonDetailViewModel(
            getPokemonFullDetailUseCase = getPokemonFullDetailUseCase,
            getIsFavoriteUseCase = getIsFavoriteUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            savedStateHandle = SavedStateHandle(mapOf("name" to pokemonName)),
        )
    }

    @Test
    fun `データ取得成功時は Success 状態になる`() {
        runTest {
            coEvery { getPokemonFullDetailUseCase("bulbasaur") } returns Result.success(fakeFullDetail)

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state.content is UiState.Success)
                val data = (state.content as UiState.Success).data
                assertEquals(fakePokemonDetail.id, data.detail.id)
                assertEquals(fakePokemonDetail.name, data.detail.name)
            }
        }
    }

    @Test
    fun `データ取得失敗時は Error 状態になる`() {
        runTest {
            coEvery { getPokemonFullDetailUseCase("bulbasaur") } returns
                Result.failure(AppException.Unknown(Exception("Not found")))

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state.content is UiState.Error)
                assertEquals("Not found", (state.content as UiState.Error).message)
            }
        }
    }

    @Test
    fun `異なるポケモン名で正しくデータを取得する`() {
        runTest {
            val charizardFullDetail =
                fakeFullDetail.copy(
                    detail = fakePokemonDetail.copy(id = 6, name = "charizard"),
                )
            coEvery { getPokemonFullDetailUseCase("charizard") } returns Result.success(charizardFullDetail)

            val viewModel = createViewModel("charizard")

            viewModel.uiState.test {
                val state = awaitItem().content as UiState.Success
                assertEquals("charizard", state.data.detail.name)
                assertEquals(6, state.data.detail.id)
            }
        }
    }

    @Test
    fun `retry で再取得できる`() {
        runTest {
            coEvery { getPokemonFullDetailUseCase("bulbasaur") } returns
                Result.failure(AppException.Unknown(Exception("error")))

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                assertTrue(awaitItem().content is UiState.Error)

                coEvery { getPokemonFullDetailUseCase("bulbasaur") } returns Result.success(fakeFullDetail)
                viewModel.retry()

                val finalState = expectMostRecentItem()
                assertTrue(finalState.content is UiState.Success)
            }
        }
    }
}

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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPokemonFullDetailUseCase = mockk<GetPokemonFullDetailUseCase>()
    private val getIsFavoriteUseCase = mockk<GetIsFavoriteUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()

    private val fakeFullDetail =
        PokemonFullDetailModel(
            detail = fakePokemonDetail,
            species = null,
            evolutionChain = emptyList(),
        )

    @Before
    fun setUp() {
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
    fun `データ取得成功時はSuccess状態になる`() =
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

    @Test
    fun `データ取得失敗時はError状態になる`() =
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

    @Test
    fun `異なるポケモン名で正しくデータを取得する`() =
        runTest {
            val charizardFullDetail =
                fakeFullDetail.copy(
                    detail = fakePokemonDetail.copy(id = 6, name = "charizard"),
                )
            coEvery { getPokemonFullDetailUseCase("charizard") } returns Result.success(charizardFullDetail)

            val viewModel = createViewModel("charizard")

            viewModel.uiState.test {
                val state = (awaitItem().content as UiState.Success).data
                assertEquals("charizard", state.detail.name)
                assertEquals(6, state.detail.id)
            }
        }

    @Test
    fun `retryで再取得できる`() =
        runTest {
            coEvery { getPokemonFullDetailUseCase("bulbasaur") } returns
                Result.failure(AppException.Unknown(Exception("error")))

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                assertTrue(awaitItem().content is UiState.Error)

                coEvery { getPokemonFullDetailUseCase("bulbasaur") } returns Result.success(fakeFullDetail)
                viewModel.retry()

                assertTrue(expectMostRecentItem().content is UiState.Success)
            }
        }

    @Test
    fun `toggleFavoriteでお気に入り状態が反転する`() =
        runTest {
            coEvery { getPokemonFullDetailUseCase("bulbasaur") } returns Result.success(fakeFullDetail)

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                assertFalse(awaitItem().isFavorite)

                viewModel.toggleFavorite()
                assertTrue(awaitItem().isFavorite)

                viewModel.toggleFavorite()
                assertFalse(awaitItem().isFavorite)
            }
        }
}

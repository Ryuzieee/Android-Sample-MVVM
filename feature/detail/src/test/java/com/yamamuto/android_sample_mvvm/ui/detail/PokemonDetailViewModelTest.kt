package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.usecase.GetEvolutionChainUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonSpeciesUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.testing.MainDispatcherRule
import com.yamamuto.android_sample_mvvm.testing.TestFixtures.fakePokemonDetail
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
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
    private lateinit var getPokemonSpeciesUseCase: GetPokemonSpeciesUseCase
    private lateinit var getEvolutionChainUseCase: GetEvolutionChainUseCase
    private lateinit var repository: com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository

    @Before
    fun setUp() {
        useCase = mockk()
        getIsFavoriteUseCase = mockk()
        toggleFavoriteUseCase = mockk()
        getPokemonSpeciesUseCase = mockk()
        getEvolutionChainUseCase = mockk()
        repository = mockk()
        every { getIsFavoriteUseCase(any()) } returns flowOf(false)
        coEvery { toggleFavoriteUseCase(any(), any()) } just Runs
        coEvery { getPokemonSpeciesUseCase(any()) } returns Result.failure(Exception("skip"))
        coEvery { getEvolutionChainUseCase(any()) } returns Result.failure(Exception("skip"))
        coEvery { repository.getAbilityJapaneseName(any()) } returns Result.success("")
    }

    private fun createViewModel(pokemonName: String = "bulbasaur"): PokemonDetailViewModel =
        PokemonDetailViewModel(
            getPokemonDetailUseCase = useCase,
            getIsFavoriteUseCase = getIsFavoriteUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            getPokemonSpeciesUseCase = getPokemonSpeciesUseCase,
            getEvolutionChainUseCase = getEvolutionChainUseCase,
            repository = repository,
            savedStateHandle = SavedStateHandle(mapOf("name" to pokemonName)),
        )

    @Test
    fun `データ取得成功時は Success 状態になる`() =
        runTest {
            coEvery { useCase("bulbasaur") } returns Result.success(fakePokemonDetail)

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state.contentState is UiState.Success)
                val data = (state.contentState as UiState.Success).data
                assertEquals(fakePokemonDetail.id, data.id)
                assertEquals(fakePokemonDetail.name, data.name)
            }
        }

    @Test
    fun `データ取得失敗時は Error 状態になる`() =
        runTest {
            coEvery { useCase("bulbasaur") } returns
                Result.failure(AppException.Unknown(Exception("Not found")))

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
            coEvery { useCase("charizard") } returns Result.success(charizardDetail)

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
            coEvery { useCase("bulbasaur") } returns
                Result.failure(AppException.Unknown(Exception("error")))

            val viewModel = createViewModel("bulbasaur")

            viewModel.uiState.test {
                assertTrue(awaitItem().contentState is UiState.Error)

                coEvery { useCase("bulbasaur") } returns Result.success(fakePokemonDetail)
                viewModel.retry()

                // ability 日本語名取得でも state が更新されるため skipItems で中間状態をスキップ
                val finalState = expectMostRecentItem()
                assertTrue(finalState.contentState is UiState.Success)
            }
        }
}

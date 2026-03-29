package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetEvolutionChainUseCaseTest {
    private val repository = mockk<PokemonRepository>()
    private val getSpeciesUseCase = mockk<GetPokemonSpeciesUseCase>()
    private val useCase = GetEvolutionChainUseCase(getSpeciesUseCase, repository)

    private fun fakeSpecies(
        jaName: String,
        chainUrl: String = "chain-url",
    ) = PokemonSpeciesModel(
        japaneseName = jaName,
        flavorText = "",
        genus = "",
        eggGroups = emptyList(),
        genderRate = -1,
        captureRate = 45,
        habitat = null,
        generation = "generation-i",
        evolutionChainUrl = chainUrl,
    )

    @Test
    fun `returns evolution stages with Japanese names`() =
        runTest {
            coEvery { getSpeciesUseCase("bulbasaur") } returns Result.success(fakeSpecies("フシギダネ", "chain-url"))
            coEvery { repository.getEvolutionChainByUrl("chain-url") } returns
                Result.success(
                    listOf(
                        EvolutionStageModel("bulbasaur", "", 1, "url1", null),
                        EvolutionStageModel("ivysaur", "", 2, "url2", 16),
                    ),
                )
            coEvery { getSpeciesUseCase("ivysaur") } returns Result.success(fakeSpecies("フシギソウ"))

            val result = useCase("bulbasaur")

            assertTrue(result.isSuccess)
            val stages = result.getOrThrow()
            assertEquals(2, stages.size)
            assertEquals("フシギダネ", stages[0].japaneseName)
            assertEquals("フシギソウ", stages[1].japaneseName)
        }

    @Test
    fun `returns failure when species fetch fails`() =
        runTest {
            coEvery { getSpeciesUseCase("bulbasaur") } returns Result.failure(AppException.Network(Exception()))

            val result = useCase("bulbasaur")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Network)
        }

    @Test
    fun `returns failure when evolution chain fetch fails`() =
        runTest {
            coEvery { getSpeciesUseCase("bulbasaur") } returns Result.success(fakeSpecies("フシギダネ"))
            coEvery { repository.getEvolutionChainByUrl(any()) } returns Result.failure(AppException.Network(Exception()))

            val result = useCase("bulbasaur")

            assertTrue(result.isFailure)
        }

    @Test
    fun `falls back to empty Japanese name when species lookup fails`() =
        runTest {
            coEvery { getSpeciesUseCase("bulbasaur") } returns Result.success(fakeSpecies("フシギダネ", "chain-url"))
            coEvery { repository.getEvolutionChainByUrl("chain-url") } returns
                Result.success(
                    listOf(EvolutionStageModel("bulbasaur", "", 1, "url1", null)),
                )

            val result = useCase("bulbasaur")

            assertTrue(result.isSuccess)
            assertEquals("フシギダネ", result.getOrThrow()[0].japaneseName)
        }
}

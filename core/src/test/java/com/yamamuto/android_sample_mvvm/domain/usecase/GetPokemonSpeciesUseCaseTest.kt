package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPokemonSpeciesUseCaseTest {
    private val repository = mockk<PokemonRepository>()
    private val useCase = GetPokemonSpeciesUseCase(repository)

    private val fakeSpecies =
        PokemonSpeciesModel(
            japaneseName = "フシギダネ",
            flavorText = "はっぱ",
            genus = "たねポケモン",
            eggGroups = listOf("monster"),
            genderRate = 1,
            captureRate = 45,
            habitat = null,
            generation = "generation-i",
            evolutionChainUrl = "https://pokeapi.co/api/v2/evolution-chain/1/",
        )

    @Test
    fun `returns species on success`() =
        runTest {
            coEvery { repository.getPokemonSpecies("bulbasaur") } returns Result.success(fakeSpecies)

            val result = useCase("bulbasaur")

            assertTrue(result.isSuccess)
            assertEquals("フシギダネ", result.getOrThrow().japaneseName)
        }

    @Test
    fun `returns failure on error`() =
        runTest {
            coEvery { repository.getPokemonSpecies(any()) } returns Result.failure(AppException.Network(Exception()))

            val result = useCase("unknown")

            assertTrue(result.isFailure)
        }
}

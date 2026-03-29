package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPokemonFullDetailUseCaseTest {
    private val getDetailUseCase = mockk<GetPokemonDetailUseCase>()
    private val getSpeciesUseCase = mockk<GetPokemonSpeciesUseCase>()
    private val getEvolutionChainUseCase = mockk<GetEvolutionChainUseCase>()
    private val getAbilityUseCase = mockk<GetAbilityJapaneseNameUseCase>()

    private val useCase =
        GetPokemonFullDetailUseCase(
            getDetailUseCase,
            getSpeciesUseCase,
            getEvolutionChainUseCase,
            getAbilityUseCase,
        )

    private val fakeDetail =
        PokemonDetailModel(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            types = listOf("grass"),
            abilities =
                listOf(
                    PokemonDetailModel.Ability("overgrow", "", false),
                ),
            imageUrl = "url",
            stats = emptyList(),
        )

    private val fakeSpecies =
        PokemonSpeciesModel(
            japaneseName = "フシギダネ",
            flavorText = "はっぱ",
            genus = "たね",
            eggGroups = emptyList(),
            genderRate = 1,
            captureRate = 45,
            habitat = null,
            generation = "generation-i",
            evolutionChainUrl = "chain-url",
        )

    @Test
    fun `全詳細取得成功時に種族情報と進化チェーンを含む結果を返す`() =
        runTest {
            coEvery { getDetailUseCase("bulbasaur", false) } returns Result.success(fakeDetail)
            coEvery { getSpeciesUseCase("bulbasaur") } returns Result.success(fakeSpecies)
            coEvery { getEvolutionChainUseCase("bulbasaur") } returns
                Result.success(
                    listOf(EvolutionStageModel("bulbasaur", "フシギダネ", 1, "url", null)),
                )
            coEvery { getAbilityUseCase("overgrow") } returns Result.success("しんりょく")

            val result = useCase("bulbasaur")

            assertTrue(result.isSuccess)
            val fullDetail = result.getOrThrow()
            assertEquals("bulbasaur", fullDetail.detail.name)
            assertEquals("しんりょく", fullDetail.detail.abilities[0].japaneseName)
            assertNotNull(fullDetail.species)
            assertEquals(1, fullDetail.evolutionChain.size)
        }

    @Test
    fun `詳細取得失敗時にエラーを返す`() =
        runTest {
            coEvery { getDetailUseCase("unknown", false) } returns
                Result.failure(
                    AppException.Network(Exception()),
                )

            val result = useCase("unknown")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Network)
        }

    @Test
    fun `種族情報取得失敗時にspeciesがnullで成功する`() =
        runTest {
            coEvery { getDetailUseCase("bulbasaur", false) } returns Result.success(fakeDetail)
            coEvery { getSpeciesUseCase("bulbasaur") } returns Result.failure(AppException.Network(Exception()))
            coEvery { getEvolutionChainUseCase("bulbasaur") } returns Result.success(emptyList())
            coEvery { getAbilityUseCase("overgrow") } returns Result.success("しんりょく")

            val result = useCase("bulbasaur")

            assertTrue(result.isSuccess)
            assertNull(result.getOrThrow().species)
        }

    @Test
    fun `進化チェーン取得失敗時に空リストで成功する`() =
        runTest {
            coEvery { getDetailUseCase("bulbasaur", false) } returns Result.success(fakeDetail)
            coEvery { getSpeciesUseCase("bulbasaur") } returns Result.success(fakeSpecies)
            coEvery { getEvolutionChainUseCase("bulbasaur") } returns Result.failure(AppException.Network(Exception()))
            coEvery { getAbilityUseCase("overgrow") } returns Result.success("しんりょく")

            val result = useCase("bulbasaur")

            assertTrue(result.isSuccess)
            assertEquals(emptyList<EvolutionStageModel>(), result.getOrThrow().evolutionChain)
        }
}

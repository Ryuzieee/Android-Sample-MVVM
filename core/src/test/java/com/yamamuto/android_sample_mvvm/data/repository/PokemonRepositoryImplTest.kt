@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.api.dto.AbilityResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.local.entity.AbilityEntry
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonNameEntity
import com.yamamuto.android_sample_mvvm.data.local.entity.StatEntry
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.InternalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class PokemonRepositoryImplTest {
    private val dataSource = mockk<PokemonRemoteDataSource>()
    private val dao = mockk<PokemonDao>()
    private val repository = PokemonRepositoryImpl(dataSource, dao)

    private val detailEntity =
        PokemonDetailEntity(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            types = listOf("grass"),
            abilities = listOf(AbilityEntry("overgrow", "", false)),
            imageUrl = "url",
            stats = listOf(StatEntry("hp", 45)),
            cachedAt = System.currentTimeMillis(),
        )

    @Test
    fun `キャッシュが存在する場合にキャッシュデータを返す`() =
        runTest {
            coEvery { dao.getPokemonDetail("bulbasaur") } returns detailEntity

            val result = repository.getPokemonDetail("bulbasaur")

            assertTrue(result.isSuccess)
            assertEquals("bulbasaur", result.getOrThrow().name)
            coVerify(exactly = 0) { dataSource.getPokemonDetail(any()) }
        }

    @Test
    fun `キャッシュがnullの場合にリモートから取得する`() =
        runTest {
            coEvery { dao.getPokemonDetail("bulbasaur") } returns null
            coEvery { dataSource.getPokemonDetail("bulbasaur") } returns createDetailResponse()
            coEvery { dao.insertPokemonDetail(any()) } just Runs

            val result = repository.getPokemonDetail("bulbasaur")

            assertTrue(result.isSuccess)
            assertEquals("bulbasaur", result.getOrThrow().name)
            coVerify { dao.insertPokemonDetail(any()) }
        }

    @Test
    fun `IOExceptionが発生した場合にNetworkエラーを返す`() =
        runTest {
            coEvery { dao.getPokemonDetail("bulbasaur") } returns null
            coEvery { dataSource.getPokemonDetail("bulbasaur") } throws IOException("timeout")

            val result = repository.getPokemonDetail("bulbasaur")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Network)
        }

    @Test
    fun `ポケモン種族情報を正しく取得する`() =
        runTest {
            coEvery { dataSource.getPokemonSpecies("bulbasaur") } returns createSpeciesResponse()

            val result = repository.getPokemonSpecies("bulbasaur")

            assertTrue(result.isSuccess)
            assertEquals("フシギダネ", result.getOrThrow().japaneseName)
        }

    @Test
    fun `進化チェーンのステージを正しく取得する`() =
        runTest {
            coEvery { dataSource.getEvolutionChain("url") } returns
                EvolutionChainResponse(
                    id = 1,
                    chain =
                        EvolutionChainResponse.ChainLink(
                            species = EvolutionChainResponse.Species("bulbasaur", "https://pokeapi.co/api/v2/pokemon-species/1/"),
                            evolvesTo = emptyList(),
                        ),
                )

            val result = repository.getEvolutionChainByUrl("url")

            assertTrue(result.isSuccess)
            assertEquals(1, result.getOrThrow().size)
            assertEquals("bulbasaur", result.getOrThrow()[0].name)
        }

    @Test
    fun `特性のローカライズ名マップを正しく取得する`() =
        runTest {
            coEvery { dataSource.getAbility("overgrow") } returns
                AbilityResponse(
                    names =
                        listOf(
                            AbilityResponse.Name("しんりょく", PokemonSpeciesResponse.NamedResource("ja")),
                            AbilityResponse.Name("Overgrow", PokemonSpeciesResponse.NamedResource("en")),
                        ),
                )

            val result = repository.getAbilityLocalizedNames("overgrow")

            assertTrue(result.isSuccess)
            assertEquals("しんりょく", result.getOrThrow()["ja"])
        }

    @Test
    fun `キャッシュされた名前を使ってポケモン名を検索する`() =
        runTest {
            coEvery { dao.getAllPokemonNames() } returns
                listOf(
                    PokemonNameEntity("bulbasaur"),
                    PokemonNameEntity("charmander"),
                    PokemonNameEntity("pikachu"),
                )

            val result = repository.searchPokemonNames("char")

            assertTrue(result.isSuccess)
            assertEquals(listOf("charmander"), result.getOrThrow())
        }

    private fun createDetailResponse() =
        PokemonDetailResponse(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            types = listOf(PokemonDetailResponse.TypeSlot(PokemonDetailResponse.TypeInfo("grass"))),
            abilities =
                listOf(
                    PokemonDetailResponse.AbilitySlot(PokemonDetailResponse.AbilityInfo("overgrow"), false),
                ),
            sprites =
                PokemonDetailResponse.Sprites(
                    PokemonDetailResponse.Sprites.Other(
                        PokemonDetailResponse.Sprites.Other.OfficialArtwork("url"),
                    ),
                ),
            stats = listOf(PokemonDetailResponse.StatSlot(45, PokemonDetailResponse.StatInfo("hp"))),
        )

    private fun createSpeciesResponse() =
        PokemonSpeciesResponse(
            names =
                listOf(
                    PokemonSpeciesResponse.Name("フシギダネ", PokemonSpeciesResponse.NamedResource("ja")),
                ),
            flavorTextEntries =
                listOf(
                    PokemonSpeciesResponse.FlavorTextEntry(
                        "はっぱ",
                        PokemonSpeciesResponse.NamedResource("ja"),
                        PokemonSpeciesResponse.NamedResource("red"),
                    ),
                ),
            evolutionChain = PokemonSpeciesResponse.EvolutionChainRef("url"),
            genera = listOf(PokemonSpeciesResponse.Genus("たねポケモン", PokemonSpeciesResponse.NamedResource("ja"))),
            eggGroups = listOf(PokemonSpeciesResponse.NamedResource("monster")),
            genderRate = 1,
            captureRate = 45,
            habitat = null,
            generation = PokemonSpeciesResponse.NamedResource("generation-i"),
        )
}

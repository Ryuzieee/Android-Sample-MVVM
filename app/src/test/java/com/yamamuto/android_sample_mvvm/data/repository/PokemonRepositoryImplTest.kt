package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonDetailResponse
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonListResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * [PokemonRepositoryImpl] の単体テスト。
 *
 * DTOからドメインモデルへのマッピングが正しく行われることを検証する。
 */
class PokemonRepositoryImplTest {
    private lateinit var dataSource: PokemonRemoteDataSource
    private lateinit var dao: PokemonDao
    private lateinit var repository: PokemonRepositoryImpl

    @Before
    fun setUp() {
        dataSource = mockk()
        dao = mockk()
        repository = PokemonRepositoryImpl(dataSource, dao)
    }

    @Test
    fun `getPokemonList はDTOをドメインモデルに正しく変換する`() =
        runTest {
            coEvery { dataSource.getPokemonList(20, 0) } returns fakePokemonListResponse

            val result = repository.getPokemonList(20, 0)

            assertEquals(1, result.size)
            assertEquals("bulbasaur", result[0].name)
            assertEquals(1, result[0].id)
            assertEquals(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                result[0].imageUrl,
            )
        }

    @Test
    fun `getPokemonDetail はDTOをドメインモデルに正しく変換する`() =
        runTest {
            coEvery { dataSource.getPokemonDetail("bulbasaur") } returns fakePokemonDetailResponse

            val result = repository.getPokemonDetail("bulbasaur")

            assertEquals(1, result.id)
            assertEquals("bulbasaur", result.name)
            assertEquals(7, result.height)
            assertEquals(69, result.weight)
            assertEquals(listOf("grass", "poison"), result.types)
            assertEquals(2, result.stats.size)
            assertEquals("hp", result.stats[0].name)
            assertEquals(45, result.stats[0].value)
        }
}

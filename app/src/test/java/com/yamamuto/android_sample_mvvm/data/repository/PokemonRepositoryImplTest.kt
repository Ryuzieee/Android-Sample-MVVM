package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonDetailResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
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
    fun `getPokemonDetail はDTOをドメインモデルに正しく変換する`() =
        runTest {
            coEvery { dao.getPokemonDetail("bulbasaur") } returns null
            coEvery { dataSource.getPokemonDetail("bulbasaur") } returns fakePokemonDetailResponse
            coEvery { dao.insertPokemonDetail(any()) } just Runs

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

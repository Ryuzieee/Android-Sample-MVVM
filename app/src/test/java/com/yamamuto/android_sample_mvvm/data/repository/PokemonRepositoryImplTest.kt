@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonDetailResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.InternalSerializationApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

            assertTrue(result.isSuccess)
            val detail = result.getOrThrow()
            assertEquals(1, detail.id)
            assertEquals("bulbasaur", detail.name)
            assertEquals(7, detail.height)
            assertEquals(69, detail.weight)
            assertEquals(listOf("grass", "poison"), detail.types)
            assertEquals(2, detail.stats.size)
            assertEquals("hp", detail.stats[0].name)
            assertEquals(45, detail.stats[0].value)
        }
}

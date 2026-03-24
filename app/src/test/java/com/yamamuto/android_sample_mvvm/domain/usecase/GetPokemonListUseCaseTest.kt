package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonList
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/** [GetPokemonListUseCase] の単体テスト。 */
class GetPokemonListUseCaseTest {
    private lateinit var repository: PokemonRepository
    private lateinit var useCase: GetPokemonListUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPokemonListUseCase(repository)
    }

    @Test
    fun `デフォルト引数でリポジトリを呼び出し結果を返す`() =
        runTest {
            coEvery { repository.getPokemonList(limit = 20, offset = 0) } returns fakePokemonList

            val result = useCase()

            assertEquals(fakePokemonList, result)
            coVerify(exactly = 1) { repository.getPokemonList(limit = 20, offset = 0) }
        }

    @Test
    fun `指定した引数でリポジトリを呼び出す`() =
        runTest {
            coEvery { repository.getPokemonList(limit = 10, offset = 20) } returns fakePokemonList

            val result = useCase(limit = 10, offset = 20)

            assertEquals(fakePokemonList, result)
            coVerify(exactly = 1) { repository.getPokemonList(limit = 10, offset = 20) }
        }

    @Test(expected = Exception::class)
    fun `リポジトリが例外をスローした場合は例外を伝播する`() =
        runTest {
            coEvery { repository.getPokemonList(any(), any()) } throws Exception("Network error")

            useCase()
        }
}

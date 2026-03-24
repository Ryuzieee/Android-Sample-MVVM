package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonDetail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/** [GetPokemonDetailUseCase] の単体テスト。 */
class GetPokemonDetailUseCaseTest {
    private lateinit var repository: PokemonRepository
    private lateinit var useCase: GetPokemonDetailUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPokemonDetailUseCase(repository)
    }

    @Test
    fun `ポケモン名でリポジトリを呼び出し結果を返す`() =
        runTest {
            coEvery { repository.getPokemonDetail("bulbasaur") } returns fakePokemonDetail

            val result = useCase("bulbasaur")

            assertEquals(fakePokemonDetail, result)
            coVerify(exactly = 1) { repository.getPokemonDetail("bulbasaur") }
        }

    @Test(expected = Exception::class)
    fun `リポジトリが例外をスローした場合は例外を伝播する`() =
        runTest {
            coEvery { repository.getPokemonDetail(any()) } throws Exception("Not found")

            useCase("unknown")
        }
}

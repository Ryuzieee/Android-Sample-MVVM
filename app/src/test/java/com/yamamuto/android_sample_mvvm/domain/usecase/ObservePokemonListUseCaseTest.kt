package com.yamamuto.android_sample_mvvm.domain.usecase

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import com.yamamuto.android_sample_mvvm.util.TestFixtures.fakePokemonList
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/** [ObservePokemonListUseCase] の単体テスト。 */
class ObservePokemonListUseCaseTest {
    private lateinit var repository: PokemonRepository
    private lateinit var useCase: ObservePokemonListUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = ObservePokemonListUseCase(repository)
    }

    @Test
    fun `PagingData を返す`() =
        runTest {
            every { repository.observePokemonPaging() } returns flowOf(PagingData.from(fakePokemonList))

            val items = useCase().asSnapshot()

            assertEquals(fakePokemonList.size, items.size)
            assertEquals("bulbasaur", items[0].name)
        }
}

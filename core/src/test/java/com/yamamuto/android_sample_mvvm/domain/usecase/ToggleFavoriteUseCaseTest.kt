package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private val repository = mockk<FavoriteRepository>(relaxed = true)
    private val useCase = ToggleFavoriteUseCase(repository)

    private val detail = PokemonDetailModel(
        id = 1, name = "bulbasaur", height = 7, weight = 69, baseExperience = 64,
        types = emptyList(), abilities = emptyList(), imageUrl = "url", stats = emptyList(),
    )

    @Test
    fun `removes favorite when isFavorite is true`() = runTest {
        useCase(detail, isFavorite = true)

        coVerify { repository.removeFavorite(1) }
        coVerify(exactly = 0) { repository.addFavorite(any()) }
    }

    @Test
    fun `adds favorite when isFavorite is false`() = runTest {
        useCase(detail, isFavorite = false)

        coVerify { repository.addFavorite(detail) }
        coVerify(exactly = 0) { repository.removeFavorite(any()) }
    }
}

package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import javax.inject.Inject

/** ポケモン名であいまい検索するユースケース。 */
class SearchPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository,
) {
    suspend operator fun invoke(query: String): Result<List<String>> {
        val names = repository.searchPokemonNames(query).getOrElse {
            return Result.failure(it)
        }
        if (names.isEmpty()) {
            return Result.failure(AppException.NotFound(query))
        }
        return Result.success(names)
    }
}

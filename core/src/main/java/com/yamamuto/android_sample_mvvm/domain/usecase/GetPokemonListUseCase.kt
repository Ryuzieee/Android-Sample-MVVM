package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import javax.inject.Inject

/** ポケモン一覧を offset/limit で取得するユースケース。 */
class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository,
) {
    suspend operator fun invoke(
        offset: Int,
        limit: Int,
    ): Result<List<PokemonSummaryModel>> {
        return repository.getPokemonList(offset = offset, limit = limit)
    }
}

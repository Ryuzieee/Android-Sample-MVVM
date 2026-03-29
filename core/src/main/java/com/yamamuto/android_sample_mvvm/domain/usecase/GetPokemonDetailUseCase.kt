package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import javax.inject.Inject

/** ポケモン詳細を取得するユースケース。 */
class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository,
) {
    suspend operator fun invoke(name: String, forceRefresh: Boolean = false): Result<PokemonDetailModel> {
        return repository.getPokemonDetail(name, forceRefresh)
    }
}

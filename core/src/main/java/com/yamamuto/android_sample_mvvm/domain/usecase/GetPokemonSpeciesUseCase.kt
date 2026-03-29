package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import javax.inject.Inject

/** ポケモン種族情報（図鑑テキスト等）を取得するユースケース。 */
class GetPokemonSpeciesUseCase @Inject constructor(
    private val repository: PokemonRepository,
) {
    suspend operator fun invoke(name: String): Result<PokemonSpeciesModel> {
        return repository.getPokemonSpecies(name)
    }
}

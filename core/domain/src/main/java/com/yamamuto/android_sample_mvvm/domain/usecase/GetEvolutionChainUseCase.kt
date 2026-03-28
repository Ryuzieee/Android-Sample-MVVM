package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStage
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import javax.inject.Inject

/** ポケモンの進化チェーンを取得するユースケース。 */
class GetEvolutionChainUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        suspend operator fun invoke(name: String): Result<List<EvolutionStage>> =
            repository.getEvolutionChain(name)
    }

package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStage
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * ポケモンの進化チェーンを取得するユースケース。
 *
 * Species → 進化チェーンURL → チェーン取得 → 各ステージの日本語名を並列解決。
 */
class GetEvolutionChainUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        suspend operator fun invoke(name: String): Result<List<EvolutionStage>> {
            val species = repository.getPokemonSpecies(name).getOrElse { return Result.failure(it) }
            val stages = repository.getEvolutionChainByUrl(species.evolutionChainUrl)
                .getOrElse { return Result.failure(it) }

            val jaNames = coroutineScope {
                stages.map { stage ->
                    async {
                        repository.getPokemonSpecies(stage.name)
                            .map { it.japaneseName }
                            .getOrDefault("")
                    }
                }.awaitAll()
            }
            return Result.success(
                stages.zip(jaNames) { stage, jaName -> stage.copy(japaneseName = jaName) },
            )
        }
    }

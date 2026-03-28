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
 * Repository から進化チェーン（日本語名なし）を取得し、
 * 各ステージの日本語名を並列で解決して結合する。
 */
class GetEvolutionChainUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        suspend operator fun invoke(name: String): Result<List<EvolutionStage>> {
            val chainResult = repository.getEvolutionChain(name)
            return chainResult.mapCatching { stages ->
                val japaneseNames = coroutineScope {
                    stages.map { stage ->
                        async {
                            repository.getSpeciesJapaneseName(stage.name).getOrDefault("")
                        }
                    }.awaitAll()
                }
                stages.zip(japaneseNames) { stage, jaName ->
                    stage.copy(japaneseName = jaName)
                }
            }
        }
    }

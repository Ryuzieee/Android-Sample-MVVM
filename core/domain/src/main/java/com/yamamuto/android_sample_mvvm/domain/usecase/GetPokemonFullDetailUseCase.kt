package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.PokemonFullDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * 詳細画面に必要な全データを一括取得するユースケース。
 *
 * 基本情報取得後、種族情報・進化チェーン・特性日本語名を並列で取得する。
 */
class GetPokemonFullDetailUseCase
    @Inject
    constructor(
        private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
        private val getPokemonSpeciesUseCase: GetPokemonSpeciesUseCase,
        private val getEvolutionChainUseCase: GetEvolutionChainUseCase,
        private val getAbilityJapaneseNameUseCase: GetAbilityJapaneseNameUseCase,
    ) {
        suspend operator fun invoke(
            name: String,
            forceRefresh: Boolean = false,
        ): Result<PokemonFullDetail> {
            val detail = getPokemonDetailUseCase(name, forceRefresh)
                .getOrElse { return Result.failure(it) }

            return coroutineScope {
                val speciesDeferred = async { getPokemonSpeciesUseCase(name).getOrNull() }
                val chainDeferred = async { getEvolutionChainUseCase(name).getOrNull() }
                val abilitiesDeferred = async { resolveAbilityNames(detail) }

                Result.success(
                    PokemonFullDetail(
                        detail = abilitiesDeferred.await(),
                        species = speciesDeferred.await(),
                        evolutionChain = chainDeferred.await() ?: emptyList(),
                    ),
                )
            }
        }

        private suspend fun resolveAbilityNames(
            detail: com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail,
        ): com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail = coroutineScope {
            val jaNames = detail.abilities.map { ability ->
                async { getAbilityJapaneseNameUseCase(ability.name).getOrDefault(ability.name) }
            }.map { it.await() }
            val updatedAbilities = detail.abilities.zip(jaNames) { ability, jaName ->
                ability.copy(japaneseName = jaName)
            }
            detail.copy(abilities = updatedAbilities)
        }
    }

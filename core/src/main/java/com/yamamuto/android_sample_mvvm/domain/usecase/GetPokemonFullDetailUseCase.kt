package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonFullDetailModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

/**
 * 詳細画面に必要な全データを一括取得するユースケース。
 *
 * 必須データと補足データの方針:
 *  - `detail` (基本情報) は **必須**。取得に失敗したら全体を [Result.failure] で返す。
 *  - `species` / `evolutionChain` / 各 ability の日本語名は **補足**。
 *    取得に失敗しても警告ログを出して既定値で続行する (画面は detail だけで成立する)。
 */
class GetPokemonFullDetailUseCase @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val getPokemonSpeciesUseCase: GetPokemonSpeciesUseCase,
    private val getEvolutionChainUseCase: GetEvolutionChainUseCase,
    private val getAbilityJapaneseNameUseCase: GetAbilityJapaneseNameUseCase,
) {
    suspend operator fun invoke(
        name: String,
        forceRefresh: Boolean = false,
    ): Result<PokemonFullDetailModel> {
        val detail =
            getPokemonDetailUseCase(name, forceRefresh)
                .getOrElse { return Result.failure(it) }

        return coroutineScope {
            val speciesDeferred = async { getPokemonSpeciesUseCase(name).orWarn("species:$name") }
            val chainDeferred = async { getEvolutionChainUseCase(name).orWarn("evolutionChain:$name") }
            val abilitiesDeferred = async { resolveAbilityNames(detail) }

            Result.success(
                PokemonFullDetailModel(
                    detail = abilitiesDeferred.await(),
                    species = speciesDeferred.await(),
                    evolutionChain = chainDeferred.await() ?: emptyList(),
                ),
            )
        }
    }

    private suspend fun resolveAbilityNames(detail: PokemonDetailModel): PokemonDetailModel =
        coroutineScope {
            val jaNames =
                detail.abilities
                    .map { ability ->
                        async {
                            getAbilityJapaneseNameUseCase(ability.name)
                                .orWarn("ability:${ability.name}") ?: ability.name
                        }
                    }.map { it.await() }
            val updatedAbilities =
                detail.abilities.zip(jaNames) { ability, jaName ->
                    ability.copy(japaneseName = jaName)
                }
            detail.copy(abilities = updatedAbilities)
        }

    /** 補足データ取得の失敗を握りつぶしつつ、観測のため警告ログだけ残す。 */
    private fun <T> Result<T>.orWarn(label: String): T? =
        fold(
            onSuccess = { it },
            onFailure = { e ->
                Timber.w(e, "Optional fetch failed: %s", label)
                null
            },
        )
}

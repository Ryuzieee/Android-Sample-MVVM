package com.yamamuto.android_sample_mvvm.domain.repository

import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel

/**
 * ポケモンデータへのアクセスを抽象化するリポジトリインターフェース。
 *
 * 実装クラスはデータ取得元（API・キャッシュ等）を隠蔽し、
 * ドメイン層がデータソースに依存しない設計を実現する。
 */
interface PokemonRepository {
    suspend fun getPokemonDetail(
        name: String,
        forceRefresh: Boolean = false,
    ): Result<PokemonDetailModel>

    suspend fun getPokemonSpecies(name: String): Result<PokemonSpeciesModel>

    /** URL から進化チェーンを取得する。 */
    suspend fun getEvolutionChainByUrl(url: String): Result<List<EvolutionStageModel>>

    /** 特性のローカライズ名一覧を取得する（language → name）。 */
    suspend fun getAbilityLocalizedNames(name: String): Result<Map<String, String>>

    suspend fun searchPokemonNames(query: String): Result<List<String>>
}

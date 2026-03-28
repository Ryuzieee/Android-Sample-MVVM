package com.yamamuto.android_sample_mvvm.domain.repository

import androidx.paging.PagingData
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStage
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpecies
import kotlinx.coroutines.flow.Flow

/**
 * ポケモンデータへのアクセスを抽象化するリポジトリインターフェース。
 *
 * 実装クラスはデータ取得元（API・キャッシュ等）を隠蔽し、
 * ドメイン層がデータソースに依存しない設計を実現する。
 */
interface PokemonRepository {
    fun observePokemonPaging(): Flow<PagingData<Pokemon>>

    suspend fun getPokemonDetail(name: String, forceRefresh: Boolean = false): Result<PokemonDetail>

    suspend fun getPokemonSpecies(name: String): Result<PokemonSpecies>

    /** URL から進化チェーンを取得する。 */
    suspend fun getEvolutionChainByUrl(url: String): Result<List<EvolutionStage>>

    /** 特性のローカライズ名一覧を取得する（language → name）。 */
    suspend fun getAbilityLocalizedNames(name: String): Result<Map<String, String>>

    suspend fun searchPokemonNames(query: String): Result<List<String>>
}

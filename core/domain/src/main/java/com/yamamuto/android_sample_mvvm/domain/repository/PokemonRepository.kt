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
    fun getPokemonPagingData(): Flow<PagingData<Pokemon>>

    suspend fun getPokemonDetail(name: String, forceRefresh: Boolean = false): Result<PokemonDetail>

    suspend fun getPokemonSpecies(name: String): Result<PokemonSpecies>

    /** 進化チェーンを取得する（日本語名なし）。 */
    suspend fun getEvolutionChain(name: String): Result<List<EvolutionStage>>

    /** ポケモンの日本語名を取得する。 */
    suspend fun getSpeciesJapaneseName(name: String): Result<String>

    /** 特性の日本語名を取得する。 */
    suspend fun getAbilityJapaneseName(name: String): Result<String>

    suspend fun searchPokemonNames(query: String): Result<List<String>>
}

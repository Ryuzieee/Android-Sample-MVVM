package com.yamamuto.android_sample_mvvm.domain.repository

import androidx.paging.PagingData
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

/**
 * ポケモンデータへのアクセスを抽象化するリポジトリインターフェース。
 *
 * 実装クラスはデータ取得元（API・キャッシュ等）を隠蔽し、
 * ドメイン層がデータソースに依存しない設計を実現する。
 */
interface PokemonRepository {
    fun getPokemonPagingData(): Flow<PagingData<Pokemon>>

    suspend fun getPokemonDetail(name: String): PokemonDetail

    suspend fun searchPokemonNames(query: String): List<String>
}

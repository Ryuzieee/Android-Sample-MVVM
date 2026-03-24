package com.yamamuto.android_sample_mvvm.domain.repository

import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail

/**
 * ポケモンデータへのアクセスを抽象化するリポジトリインターフェース。
 *
 * 実装クラスはデータ取得元（API・キャッシュ等）を隠蔽し、
 * ドメイン層がデータソースに依存しない設計を実現する。
 */
interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>
    suspend fun getPokemonDetail(name: String): PokemonDetail
}

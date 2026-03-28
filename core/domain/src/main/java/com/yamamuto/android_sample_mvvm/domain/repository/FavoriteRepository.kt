package com.yamamuto.android_sample_mvvm.domain.repository

import com.yamamuto.android_sample_mvvm.domain.model.Favorite
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

/**
 * お気に入りデータへのアクセスを抽象化するリポジトリインターフェース。
 */
interface FavoriteRepository {
    fun observeFavorites(): Flow<List<Favorite>>

    fun observeIsFavorite(id: Int): Flow<Boolean>

    suspend fun addFavorite(detail: PokemonDetail)

    suspend fun removeFavorite(id: Int)
}

package com.yamamuto.android_sample_mvvm.domain.repository

import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import kotlinx.coroutines.flow.Flow

/**
 * お気に入りデータへのアクセスを抽象化するリポジトリインターフェース。
 */
interface FavoriteRepository {
    fun observeFavorites(): Flow<List<FavoriteModel>>

    fun observeIsFavorite(id: Int): Flow<Boolean>

    suspend fun addFavorite(detail: PokemonDetailModel)

    suspend fun removeFavorite(id: Int)
}

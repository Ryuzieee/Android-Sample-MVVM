package com.yamamuto.android_sample_mvvm.domain.repository

import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel

/**
 * お気に入りデータへのアクセスを抽象化するリポジトリインターフェース。
 */
interface FavoriteRepository {
    suspend fun getFavorites(): Result<List<FavoriteModel>>

    suspend fun isFavorite(id: Int): Result<Boolean>

    suspend fun addFavorite(detail: PokemonDetailModel)

    suspend fun removeFavorite(id: Int)
}

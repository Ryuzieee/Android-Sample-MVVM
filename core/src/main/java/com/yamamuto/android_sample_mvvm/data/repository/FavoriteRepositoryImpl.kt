package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.local.dao.FavoriteDao
import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import javax.inject.Inject

/** [FavoriteRepository] の実装クラス。Room を使ってお気に入りを永続化する。 */
class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao,
) : FavoriteRepository {
    override suspend fun getFavorites(): List<FavoriteModel> {
        return dao.getAllFavorites().map { FavoriteModel(id = it.id, name = it.name, imageUrl = it.imageUrl) }
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return dao.isFavorite(id)
    }

    override suspend fun addFavorite(detail: PokemonDetailModel) {
        dao.insertFavorite(
            FavoriteEntity(
                id = detail.id,
                name = detail.name,
                imageUrl = detail.imageUrl,
            ),
        )
    }

    override suspend fun removeFavorite(id: Int) {
        dao.deleteFavorite(id)
    }
}

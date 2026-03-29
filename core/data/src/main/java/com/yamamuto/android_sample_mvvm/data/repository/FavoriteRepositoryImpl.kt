package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.local.dao.FavoriteDao
import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** [FavoriteRepository] の実装クラス。Room を使ってお気に入りを永続化する。 */
class FavoriteRepositoryImpl
    @Inject
    constructor(
        private val dao: FavoriteDao,
    ) : FavoriteRepository {
    override fun observeFavorites(): Flow<List<FavoriteModel>> {
        return dao.getAllFavorites().map { list ->
            list.map { FavoriteModel(id = it.id, name = it.name, imageUrl = it.imageUrl) }
        }
    }

    override fun observeIsFavorite(id: Int): Flow<Boolean> {
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

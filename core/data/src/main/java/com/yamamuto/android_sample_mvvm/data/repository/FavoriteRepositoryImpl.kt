package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.local.dao.FavoriteDao
import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import com.yamamuto.android_sample_mvvm.domain.model.Favorite
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** [FavoriteRepository] の実装クラス。Room を使ってお気に入りを永続化する。 */
class FavoriteRepositoryImpl(
    private val dao: FavoriteDao,
) : FavoriteRepository {
    override fun getFavorites(): Flow<List<Favorite>> =
        dao.getAllFavorites().map { list ->
            list.map { Favorite(id = it.id, name = it.name, imageUrl = it.imageUrl) }
        }

    override fun isFavorite(id: Int): Flow<Boolean> = dao.isFavorite(id)

    override suspend fun addFavorite(detail: PokemonDetail) {
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

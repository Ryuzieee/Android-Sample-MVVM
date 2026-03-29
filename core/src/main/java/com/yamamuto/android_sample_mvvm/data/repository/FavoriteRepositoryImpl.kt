package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.local.dao.FavoriteDao
import com.yamamuto.android_sample_mvvm.data.mapper.toEntity
import com.yamamuto.android_sample_mvvm.data.mapper.toModel
import com.yamamuto.android_sample_mvvm.data.util.handleLocal
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import javax.inject.Inject

/** [FavoriteRepository] の実装クラス。Room を使ってお気に入りを永続化する。 */
class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao,
) : FavoriteRepository {
    override suspend fun getFavorites(): Result<List<FavoriteModel>> {
        return handleLocal(
            query = { dao.getAllFavorites() },
            toModel = { list -> list.map { it.toModel() } },
        )
    }

    override suspend fun isFavorite(id: Int): Result<Boolean> {
        return handleLocal(
            query = { dao.isFavorite(id) },
            toModel = { it },
        )
    }

    override suspend fun addFavorite(detail: PokemonDetailModel) {
        dao.insertFavorite(detail.toEntity())
    }

    override suspend fun removeFavorite(id: Int) {
        dao.deleteFavorite(id)
    }
}

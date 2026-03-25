package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.Favorite
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** お気に入り一覧を取得するユースケース。 */
class GetFavoritesUseCase
    @Inject
    constructor(
        private val repository: FavoriteRepository,
    ) {
        operator fun invoke(): Flow<List<Favorite>> = repository.getFavorites()
    }

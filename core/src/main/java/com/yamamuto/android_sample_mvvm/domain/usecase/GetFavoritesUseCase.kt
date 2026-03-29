package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import javax.inject.Inject

/** お気に入り一覧を取得するユースケース。 */
class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(): List<FavoriteModel> {
        return repository.getFavorites()
    }
}

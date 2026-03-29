package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import javax.inject.Inject

/** 指定ポケモンがお気に入りかどうかを取得するユースケース。 */
class GetIsFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(id: Int): Boolean {
        return repository.isFavorite(id)
    }
}

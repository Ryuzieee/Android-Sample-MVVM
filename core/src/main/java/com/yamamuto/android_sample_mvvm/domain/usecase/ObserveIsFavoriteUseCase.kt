package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** 指定ポケモンがお気に入りかどうかを監視するユースケース。 */
class ObserveIsFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    operator fun invoke(id: Int): Flow<Boolean> {
        return repository.observeIsFavorite(id)
    }
}

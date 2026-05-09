package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import javax.inject.Inject

/**
 * お気に入りをトグルするユースケース。
 *
 * [Result] で返すことで、UI 側が DB 書き込み失敗を検知してロールバック等の
 * リカバリ処理を行えるようにする。
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(
        detail: PokemonDetailModel,
        isFavorite: Boolean,
    ): Result<Unit> {
        return if (isFavorite) {
            repository.removeFavorite(detail.id)
        } else {
            repository.addFavorite(detail)
        }
    }
}

package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import javax.inject.Inject

/**
 * お気に入りをトグルするユースケース。
 *
 * [isFavorite] が true なら削除、false なら追加する。
 */
class ToggleFavoriteUseCase
    @Inject
    constructor(
        private val repository: FavoriteRepository,
    ) {
        suspend operator fun invoke(
            detail: PokemonDetail,
            isFavorite: Boolean,
        ) {
            if (isFavorite) {
                repository.removeFavorite(detail.id)
            } else {
                repository.addFavorite(detail)
            }
        }
    }

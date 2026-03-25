package com.yamamuto.android_sample_mvvm.ui.favorites

import com.yamamuto.android_sample_mvvm.domain.model.Favorite

/** お気に入り一覧画面のUI状態。 */
data class FavoritesUiState(
    val favorites: List<Favorite> = emptyList(),
)

package com.yamamuto.android_sample_mvvm.ui.favorites

import com.yamamuto.android_sample_mvvm.domain.model.Favorite
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/** お気に入り一覧画面のUI状態。 */
data class FavoritesUiState(
    val contentState: UiState<List<Favorite>> = UiState.Loading,
)

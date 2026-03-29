package com.yamamuto.android_sample_mvvm.ui.detail

import com.yamamuto.android_sample_mvvm.domain.model.PokemonFullDetailModel
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/** ポケモン詳細画面のUI状態。 */
data class PokemonDetailUiState(
    val contentState: UiState<PokemonFullDetailModel> = UiState.Loading,
    val isFavorite: Boolean = false,
    val isRefreshing: Boolean = false,
)

package com.yamamuto.android_sample_mvvm.ui.detail

import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.UiState

/** ポケモン詳細画面のUI状態。 */
data class PokemonDetailUiState(
    val contentState: UiState<PokemonDetail> = UiState.Loading,
    val isFavorite: Boolean = false,
)

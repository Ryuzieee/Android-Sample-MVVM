package com.yamamuto.android_sample_mvvm.ui.list

import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/** ポケモン一覧画面のUI状態。 */
data class PokemonListUiState(
    val items: List<PokemonSummaryModel> = emptyList(),
    val loadState: UiState<Unit> = UiState.Idle,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasMore: Boolean = true,
)

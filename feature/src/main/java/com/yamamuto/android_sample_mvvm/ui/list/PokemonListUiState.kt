package com.yamamuto.android_sample_mvvm.ui.list

import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/**
 * ポケモン一覧画面のUI状態。
 *
 * 取得済みアイテムは [loadState] が [UiState.Success] のときの data に含まれる。
 * ページング固有の状態 ([isLoadingMore] / [hasMore]) と pull-to-refresh 状態
 * ([isRefreshing]) は UiState とは直交する別フィールドで持つ。
 */
data class PokemonListUiState(
    val loadState: UiState<List<PokemonSummaryModel>> = UiState.Loading,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasMore: Boolean = true,
)

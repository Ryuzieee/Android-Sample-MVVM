package com.yamamuto.android_sample_mvvm.ui.search

import com.yamamuto.android_sample_mvvm.domain.model.UiState

/** 検索画面のUI状態。[result] が [UiState.Idle] の場合は入力待ちのアイドル状態を表す。 */
data class SearchUiState(
    val query: String = "",
    val result: UiState<List<String>> = UiState.Idle,
)

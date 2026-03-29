package com.yamamuto.android_sample_mvvm.ui.list

import androidx.paging.PagingData
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel

/** ポケモン一覧画面のUI状態。 */
data class PokemonListUiState(
    val pagingData: PagingData<PokemonSummaryModel> = PagingData.empty(),
    val isRefreshing: Boolean = false,
)

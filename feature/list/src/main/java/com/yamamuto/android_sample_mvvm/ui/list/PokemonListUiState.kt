package com.yamamuto.android_sample_mvvm.ui.list

import androidx.paging.PagingData
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

/**
 * ポケモン一覧画面のUI状態。
 *
 * [pagingData] は ViewModel が [androidx.paging.cachedIn] で保持する同一 Flow オブジェクトを参照するため、
 * 画面再構成時にページングがリセットされることはない。
 */
data class PokemonListUiState(
    val pagingData: Flow<PagingData<Pokemon>>,
)

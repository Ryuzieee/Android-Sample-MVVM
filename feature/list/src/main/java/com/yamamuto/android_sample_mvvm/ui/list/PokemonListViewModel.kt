package com.yamamuto.android_sample_mvvm.ui.list

import com.yamamuto.android_sample_mvvm.data.paging.PagingSourceFactory
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val PAGE_SIZE = 20

/** ポケモン一覧画面のViewModel。 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pagingSourceFactory: PagingSourceFactory<PokemonSummaryModel>,
) : UiStateViewModel<PokemonListUiState>(PokemonListUiState()) {
    init {
        collectPaging()
    }

    private fun collectPaging() {
        collectPaging(
            pageSize = PAGE_SIZE,
            source = { pagingSourceFactory.create() },
        ) { copy(pagingData = it) }
    }
}

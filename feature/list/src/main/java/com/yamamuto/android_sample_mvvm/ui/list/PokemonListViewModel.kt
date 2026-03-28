package com.yamamuto.android_sample_mvvm.ui.list

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yamamuto.android_sample_mvvm.data.paging.PokemonPagingSourceFactory
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGE_SIZE = 20

/** ポケモン一覧画面のViewModel。 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pagingSourceFactory: PokemonPagingSourceFactory,
) : UiStateViewModel<PokemonListUiState>(PokemonListUiState()) {
    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = { pagingSourceFactory.create() },
            ).flow
                .cachedIn(viewModelScope)
                .collect { updateState { copy(pagingData = it) } }
        }
    }
}

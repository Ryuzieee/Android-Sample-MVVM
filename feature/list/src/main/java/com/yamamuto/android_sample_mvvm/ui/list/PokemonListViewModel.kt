package com.yamamuto.android_sample_mvvm.ui.list

import androidx.lifecycle.ViewModel
import com.yamamuto.android_sample_mvvm.data.paging.PagingSourceFactory
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import com.yamamuto.android_sample_mvvm.ui.util.collectPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val PAGE_SIZE = 20

/** ポケモン一覧画面のViewModel。 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pagingSourceFactory: PagingSourceFactory<PokemonSummaryModel>,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        collectPaging()
    }

    private fun collectPaging() {
        collectPaging(
            state = _uiState,
            pageSize = PAGE_SIZE,
            source = { pagingSourceFactory.create() },
            transform = { copy(pagingData = it) }
        )
    }
}

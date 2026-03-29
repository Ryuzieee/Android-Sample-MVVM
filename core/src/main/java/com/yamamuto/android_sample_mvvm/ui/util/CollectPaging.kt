package com.yamamuto.android_sample_mvvm.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Paging Flow を購読して UiState に反映する ViewModel 拡張関数。
 */
fun <S, T : Any> ViewModel.collectPaging(
    state: MutableStateFlow<S>,
    pageSize: Int,
    source: () -> PagingSource<Int, T>,
    transform: S.(PagingData<T>) -> S,
) {
    viewModelScope.launch {
        Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = source,
        ).flow
            .cachedIn(viewModelScope)
            .collect { data -> state.update { it.transform(data) } }
    }
}

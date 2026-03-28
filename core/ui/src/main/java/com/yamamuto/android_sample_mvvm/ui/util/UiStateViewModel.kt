package com.yamamuto.android_sample_mvvm.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UiState / UiEvent の定型コードを集約した ViewModel 基底クラス。
 *
 * サブクラスは [updateState] / [sendEvent] だけを使えばよく、
 * MutableStateFlow / Channel の宣言を繰り返す必要がない。
 */
abstract class UiStateViewModel<S>(initialState: S) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    protected val currentState: S get() {
        return _uiState.value
    }

    protected fun updateState(transform: S.() -> S) {
        _uiState.update(transform)
    }

    protected suspend fun sendEvent(event: UiEvent) {
        _events.send(event)
    }

    /**
     * Paging Flow を購読して UiState に反映するヘルパー。
     *
     * [Pager] / [PagingConfig] / [cachedIn] の定型コードを吸収する。
     *
     * ```
     * init {
     *     collectPaging(
     *         pageSize = PAGE_SIZE,
     *         source = { pagingSourceFactory.create() },
     *     ) { copy(pagingData = it) }
     * }
     * ```
     */
    protected fun <T : Any> collectPaging(
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
                .collect { data -> updateState { transform(data) } }
        }
    }
}

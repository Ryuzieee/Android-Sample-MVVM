package com.yamamuto.android_sample_mvvm.ui.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

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

    protected val currentState: S get() = _uiState.value

    protected fun updateState(transform: S.() -> S) {
        _uiState.update(transform)
    }

    protected suspend fun sendEvent(event: UiEvent) {
        _events.send(event)
    }
}

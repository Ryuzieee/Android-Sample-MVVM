package com.yamamuto.android_sample_mvvm.ui.base

import androidx.lifecycle.ViewModel
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * 全画面共通の ViewModel 基底クラス。
 *
 * [uiState] で画面の状態を、[events] で一回限りのイベントを公開する。
 * サブクラスは [updateState] / [sendEvent] を通じて状態・イベントを更新する。
 */
abstract class BaseViewModel<S>(initialState: S) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    protected val currentState: S get() = _uiState.value

    /**
     * 状態を新しいオブジェクトで丸ごと置き換える。
     *
     * ローディング完了後に全フィールドを確定した値で上書きするときなど、
     * 現在の状態を参照せずに次の状態が決まる場合に使う。
     *
     * 例:
     * ```
     * updateState(PokemonDetailUiState(UiState.Success(detail), isFavorite))
     * ```
     */
    protected fun updateState(newState: S) {
        _uiState.value = newState
    }

    /**
     * 現在の状態を元に一部フィールドだけ変更する。
     *
     * data class の `copy()` と組み合わせて、変更したいフィールドだけ上書きし
     * 残りは現在の値を維持したい場合に使う。
     *
     * 例:
     * ```
     * updateState { it.copy(contentState = UiState.Loading) }
     * updateState { it.copy(result = UiState.Error(...)) }
     * ```
     */
    protected fun updateState(transform: (S) -> S) {
        _uiState.value = transform(_uiState.value)
    }

    protected suspend fun sendEvent(event: UiEvent) {
        _events.send(event)
    }
}

package com.yamamuto.android_sample_mvvm.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * SnackBar 表示やナビゲーションなどの一回限りイベント。
 *
 * ViewModel で [kotlinx.coroutines.channels.Channel] + [kotlinx.coroutines.flow.receiveAsFlow]
 * と組み合わせて使用する。
 */
sealed interface UiEvent {
    /** SnackBar を表示するイベント。 */
    data class ShowSnackbar(val message: String) : UiEvent

    /** 画面を戻るイベント。 */
    data object NavigateBack : UiEvent
}

/**
 * [Flow] から一回限りイベントを安全に収集する Composable ヘルパー。
 *
 * ライフサイクルを考慮し、STARTED 以上の場合のみイベントを処理する。
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    onEvent: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}

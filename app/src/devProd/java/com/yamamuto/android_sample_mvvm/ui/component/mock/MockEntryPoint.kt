package com.yamamuto.android_sample_mvvm.ui.component.mock

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable

/**
 * dev / prod フレーバーでは何も表示しないスタブ。
 *
 * mock フレーバーでは `app/src/mock/` 側の同名関数が代わりにコンパイルされ、
 * 画面右下にモックシナリオ切替 FAB を表示する。
 */
@Composable
@Suppress("UnusedReceiverParameter")
fun BoxScope.MockEntryPoint() {
    // No-op outside the mock flavor.
}

package com.yamamuto.android_sample_mvvm.ui.util

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * セッション切れ・強制アップデートなどアプリ全体で統一的に処理するエラーハンドラ。
 *
 * [UiStateContent][com.yamamuto.android_sample_mvvm.ui.component.UiStateContent] が
 * 内部で参照するため、Activity レベルで [LocalAppErrorHandler] に提供すること。
 */
data class AppErrorHandler(
    val onSessionExpired: () -> Unit = {},
    val onForceUpdate: (storeUrl: String) -> Unit = {},
)

/** [AppErrorHandler] を Compose ツリーに提供する CompositionLocal。 */
val LocalAppErrorHandler = staticCompositionLocalOf { AppErrorHandler() }

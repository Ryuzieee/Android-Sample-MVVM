package com.yamamuto.android_sample_mvvm.domain.model

/** アプリ全体に通知する横断的イベント。 */
sealed interface AppEvent {
    /** セッション切れ。再ログインが必要。 */
    data object SessionExpired : AppEvent

    /** 強制アップデートが必要。 */
    data class ForceUpdate(val storeUrl: String) : AppEvent
}

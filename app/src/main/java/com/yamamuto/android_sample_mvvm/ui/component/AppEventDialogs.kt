package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.yamamuto.android_sample_mvvm.domain.model.AppEvent

/** [AppEvent] に応じたダイアログを表示する。NavHost より上位に配置すること。 */
@Composable
fun AppEventDialogs(
    event: AppEvent?,
    onSessionExpiredConfirm: () -> Unit,
    onForceUpdateConfirm: (storeUrl: String) -> Unit,
) {
    when (event) {
        is AppEvent.SessionExpired -> {
            AlertDialog(
                onDismissRequest = { /* 閉じさせない */ },
                title = { Text("セッション切れ") },
                text = { Text("セッションの有効期限が切れました。\n再度ログインしてください。") },
                confirmButton = {
                    TextButton(onClick = onSessionExpiredConfirm) {
                        Text("ログイン画面へ")
                    }
                },
            )
        }
        is AppEvent.ForceUpdate -> {
            AlertDialog(
                onDismissRequest = { /* 閉じさせない */ },
                title = { Text("アップデートが必要です") },
                text = { Text("新しいバージョンが利用可能です。\nアプリをアップデートしてください。") },
                confirmButton = {
                    TextButton(onClick = { onForceUpdateConfirm(event.storeUrl) }) {
                        Text("ストアを開く")
                    }
                },
            )
        }
        null -> { /* no-op */ }
    }
}

package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.yamamuto.android_sample_mvvm.domain.model.AppEvent
import com.yamamuto.android_sample_mvvm.ui.Strings

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
                title = { Text(Strings.Dialog.SESSION_EXPIRED_TITLE) },
                text = { Text(Strings.Dialog.SESSION_EXPIRED_MESSAGE) },
                confirmButton = {
                    TextButton(onClick = onSessionExpiredConfirm) {
                        Text(Strings.Dialog.SESSION_EXPIRED_BUTTON)
                    }
                },
            )
        }
        is AppEvent.ForceUpdate -> {
            AlertDialog(
                onDismissRequest = { /* 閉じさせない */ },
                title = { Text(Strings.Dialog.FORCE_UPDATE_TITLE) },
                text = { Text(Strings.Dialog.FORCE_UPDATE_MESSAGE) },
                confirmButton = {
                    TextButton(onClick = { onForceUpdateConfirm(event.storeUrl) }) {
                        Text(Strings.Dialog.FORCE_UPDATE_BUTTON)
                    }
                },
            )
        }
        null -> { /* no-op */ }
    }
}

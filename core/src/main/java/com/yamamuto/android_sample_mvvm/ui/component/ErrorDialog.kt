package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.yamamuto.android_sample_mvvm.ui.Strings

/**
 * リフレッシュ失敗時などにコンテンツの上にオーバーレイ表示するエラーダイアログ。
 */
@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { AppText(text = Strings.Common.ERROR_TITLE) },
        text = { AppText(text = message) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                AppText(text = Strings.Common.RETRY)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                AppText(text = Strings.Common.CLOSE)
            }
        },
    )
}

/**
 * セッション切れダイアログ。閉じることはできない。
 */
@Composable
fun SessionExpiredDialog(onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* 閉じさせない */ },
        title = { AppText(text = Strings.Dialog.SESSION_EXPIRED_TITLE) },
        text = { AppText(text = Strings.Dialog.SESSION_EXPIRED_MESSAGE) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                AppText(text = Strings.Dialog.SESSION_EXPIRED_BUTTON)
            }
        },
    )
}

/**
 * 強制アップデートダイアログ。閉じることはできない。
 */
@Composable
fun ForceUpdateDialog(onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* 閉じさせない */ },
        title = { AppText(text = Strings.Dialog.FORCE_UPDATE_TITLE) },
        text = { AppText(text = Strings.Dialog.FORCE_UPDATE_MESSAGE) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                AppText(text = Strings.Dialog.FORCE_UPDATE_BUTTON)
            }
        },
    )
}

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

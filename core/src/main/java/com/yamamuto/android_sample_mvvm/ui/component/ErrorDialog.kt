package com.yamamuto.android_sample_mvvm.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.yamamuto.android_sample_mvvm.ui.Strings

/** コンテンツの上にオーバーレイ表示するエラーダイアログ。 */
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

/** セッション切れダイアログ。 */
@Composable
fun SessionExpiredDialog() {
    AlertDialog(
        onDismissRequest = { /* 閉じさせない */ },
        title = { AppText(text = Strings.Dialog.SESSION_EXPIRED_TITLE) },
        text = { AppText(text = Strings.Dialog.SESSION_EXPIRED_MESSAGE) },
        confirmButton = {
            TextButton(onClick = { /* TODO: ログイン画面へ遷移 */ }) {
                AppText(text = Strings.Dialog.SESSION_EXPIRED_BUTTON)
            }
        },
    )
}

/** 強制アップデートダイアログ。 */
@Composable
fun ForceUpdateDialog(storeUrl: String) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { /* 閉じさせない */ },
        title = { AppText(text = Strings.Dialog.FORCE_UPDATE_TITLE) },
        text = { AppText(text = Strings.Dialog.FORCE_UPDATE_MESSAGE) },
        confirmButton = {
            TextButton(
                onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl))) },
            ) {
                AppText(text = Strings.Dialog.FORCE_UPDATE_BUTTON)
            }
        },
    )
}

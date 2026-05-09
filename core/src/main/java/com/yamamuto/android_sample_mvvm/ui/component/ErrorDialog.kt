package com.yamamuto.android_sample_mvvm.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.yamamuto.android_sample_mvvm.core.R

/** コンテンツの上にオーバーレイ表示するエラーダイアログ。 */
@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { AppText(text = stringResource(R.string.common_error_title)) },
        text = { AppText(text = message) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                AppText(text = stringResource(R.string.common_retry))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                AppText(text = stringResource(R.string.common_close))
            }
        },
    )
}

/** セッション切れダイアログ。 */
@Composable
fun SessionExpiredDialog() {
    AlertDialog(
        onDismissRequest = { /* 閉じさせない */ },
        title = { AppText(text = stringResource(R.string.dialog_session_expired_title)) },
        text = { AppText(text = stringResource(R.string.dialog_session_expired_message)) },
        confirmButton = {
            TextButton(onClick = { /* TODO: ログイン画面へ遷移 */ }) {
                AppText(text = stringResource(R.string.dialog_session_expired_button))
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
        title = { AppText(text = stringResource(R.string.dialog_force_update_title)) },
        text = { AppText(text = stringResource(R.string.dialog_force_update_message)) },
        confirmButton = {
            TextButton(
                onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl))) },
            ) {
                AppText(text = stringResource(R.string.dialog_force_update_button))
            }
        },
    )
}

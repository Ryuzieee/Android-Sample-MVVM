package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yamamuto.android_sample_mvvm.ui.Strings

/**
 * エラー表示とリトライボタンを備えた共通コンポーネント。
 *
 * [isNetworkError] が true の場合はオフライン用のメッセージを表示する。
 */
@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isNetworkError: Boolean = false,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppText(
            text = if (isNetworkError) Strings.Error.NETWORK_MESSAGE else message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
        if (isNetworkError) {
            AppText(
                text = Strings.Error.NETWORK_SUB_MESSAGE,
                style = MaterialTheme.typography.bodySmall,
                secondary = true,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(Strings.Common.RETRY)
        }
    }
}

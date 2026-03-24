package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * アプリ共通テキスト入力フィールド
 *
 * OutlinedTextField をラップしたアプリ標準の入力コンポーネント。
 * アプリ全体で統一されたスタイルを提供する。
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = enabled,
        singleLine = singleLine,
        modifier = modifier.fillMaxWidth(),
    )
}

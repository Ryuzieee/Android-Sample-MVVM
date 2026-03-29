package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * アプリ共通のモーダルボトムシート。
 *
 * ```kotlin
 * var show by remember { mutableStateOf(false) }
 * if (show) {
 *     AppBottomSheet(onDismiss = { show = false }) {
 *         Text("Hello")
 *     }
 * }
 * ```
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier,
    ) {
        if (title != null) {
            AppText(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                bold = true,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        content()
        // ナビゲーションバー分の余白
        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.navigationBarsPadding(),
        )
    }
}

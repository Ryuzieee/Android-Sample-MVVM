package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmptyContent(
    message: String,
    modifier: Modifier = Modifier,
    subMessage: String? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppText(text = message, style = MaterialTheme.typography.bodyLarge, secondary = true)
        if (subMessage != null) {
            AppText(
                text = subMessage,
                style = MaterialTheme.typography.bodySmall,
                secondary = true,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

package com.yamamuto.android_sample_mvvm.ui.component.mock

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * mock フレーバーで画面右下に表示する FAB。
 * タップで [MockScenarioSelector] を起動し、モックシナリオを切り替えられる。
 */
@Composable
fun BoxScope.MockEntryPoint() {
    var showSelector by remember { mutableStateOf(false) }
    FloatingActionButton(
        onClick = { showSelector = true },
        modifier =
            Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 96.dp),
    ) {
        Text("Mock")
    }
    if (showSelector) {
        MockScenarioSelector(onDismiss = { showSelector = false })
    }
}

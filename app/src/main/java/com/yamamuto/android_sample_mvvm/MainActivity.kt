package com.yamamuto.android_sample_mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamamuto.android_sample_mvvm.ui.component.mock.MockScenarioSelector
import com.yamamuto.android_sample_mvvm.ui.navigation.AppNavGraph
import com.yamamuto.android_sample_mvvm.ui.theme.AndroidSampleMVVMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var showMockSelector by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSampleMVVMTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph()
                    MockFloatingActionButton()
                }
            }
        }
    }

    @Composable
    private fun BoxScope.MockFloatingActionButton() {
        if (!BuildConfig.IS_MOCK) return
        FloatingActionButton(
            onClick = { showMockSelector = true },
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 96.dp),
        ) {
            Text("Mock")
        }
        if (showMockSelector) {
            MockScenarioSelector(
                onDismiss = { showMockSelector = false },
            )
        }
    }
}

package com.yamamuto.android_sample_mvvm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yamamuto.android_sample_mvvm.domain.model.AppEvent
import com.yamamuto.android_sample_mvvm.domain.model.AppEventBus
import com.yamamuto.android_sample_mvvm.ui.component.AppEventDialogs
import com.yamamuto.android_sample_mvvm.ui.component.MockScenarioSelector
import com.yamamuto.android_sample_mvvm.ui.navigation.AppNavGraph
import com.yamamuto.android_sample_mvvm.ui.theme.AndroidSampleMVVMTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appEventBus: AppEventBus

    private var currentEvent by mutableStateOf<AppEvent?>(null)
    private var showMockSelector by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeAppEvents()
        setContent {
            AndroidSampleMVVMTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph()

                    if (BuildConfig.IS_MOCK) {
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

                AppEventDialogs(
                    event = currentEvent,
                    onSessionExpiredConfirm = { handleSessionExpired() },
                    onForceUpdateConfirm = { url -> handleForceUpdate(url) },
                )
            }
        }
    }

    private fun observeAppEvents() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appEventBus.events.collect { event ->
                    currentEvent = event
                }
            }
        }
    }

    private fun handleSessionExpired() {
        currentEvent = null
        // TODO: ログイン画面へ遷移する処理を実装
    }

    private fun handleForceUpdate(storeUrl: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl)))
    }
}

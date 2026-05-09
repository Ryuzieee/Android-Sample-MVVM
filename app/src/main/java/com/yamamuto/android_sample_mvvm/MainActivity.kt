package com.yamamuto.android_sample_mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.yamamuto.android_sample_mvvm.ui.component.mock.MockEntryPoint
import com.yamamuto.android_sample_mvvm.ui.navigation.AppNavGraph
import com.yamamuto.android_sample_mvvm.ui.theme.AndroidSampleMVVMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSampleMVVMTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph()
                    MockEntryPoint()
                }
            }
        }
    }
}

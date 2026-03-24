package com.yamamuto.android_sample_mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yamamuto.android_sample_mvvm.ui.navigation.AppNavGraph
import com.yamamuto.android_sample_mvvm.ui.theme.AndroidSampleMVVMTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * アプリのエントリポイントとなるActivity。
 *
 * [@AndroidEntryPoint] によって Hilt が依存性を注入できるようになる。
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSampleMVVMTheme {
                AppNavGraph()
            }
        }
    }
}

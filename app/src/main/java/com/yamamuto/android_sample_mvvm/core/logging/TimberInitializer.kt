package com.yamamuto.android_sample_mvvm.core.logging

import com.yamamuto.android_sample_mvvm.BuildConfig
import timber.log.Timber

/**
 * Timber の初期化を行うユーティリティ。
 *
 * Debug ビルドのみ [Timber.DebugTree] を植える。
 * Release ビルドではログを出力しない。
 */
object TimberInitializer {
    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

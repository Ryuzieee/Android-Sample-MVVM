package com.yamamuto.android_sample_mvvm

import android.app.Application
import com.yamamuto.android_sample_mvvm.core.logging.TimberInitializer
import dagger.hilt.android.HiltAndroidApp

/**
 * アプリケーションクラス。
 *
 * [@HiltAndroidApp] によって Hilt のコンポーネント生成が行われる。
 */
@HiltAndroidApp
class PokemonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TimberInitializer.init()
    }
}

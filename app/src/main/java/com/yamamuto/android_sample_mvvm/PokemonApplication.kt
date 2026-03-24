package com.yamamuto.android_sample_mvvm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * アプリケーションクラス。
 *
 * [@HiltAndroidApp] によって Hilt のコンポーネント生成が行われる。
 * このクラスを AndroidManifest.xml の android:name に指定する必要がある。
 */
@HiltAndroidApp
class PokemonApplication : Application()

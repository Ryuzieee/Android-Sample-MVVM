package com.yamamuto.android_sample_mvvm.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit4.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Baseline Profile を生成するテストクラス。
 *
 * 実行方法:
 * ```
 * ./gradlew :benchmark:pixel6Api31BenchmarkAndroidTest
 * ```
 *
 * 生成されたプロファイルは :app の src/main/baseline-prof.txt にコピーして使用する。
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() {
        baselineProfileRule.collect(packageName = "com.yamamuto.android_sample_mvvm") {
            startActivityAndWait()
            // TODO: 画面遷移などの主要なユーザーフローを追加
        }
    }
}

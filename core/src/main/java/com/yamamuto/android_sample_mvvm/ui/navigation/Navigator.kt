package com.yamamuto.android_sample_mvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * [NavHostController] を薄くラップして「進む / 戻る」の口を一本化するナビゲータ。
 *
 * 各 feature の Navigation 定義はこの Navigator だけを受け取れば配線できるため、
 * AppNavGraph が画面ごとの onClick コールバックを個別に書き並べる必要がなくなる。
 */
class Navigator(val navController: NavHostController) {
    fun navigate(route: Any) {
        navController.navigate(route)
    }

    fun back() {
        navController.popBackStack()
    }
}

@Composable
fun rememberAppNavigator(): Navigator {
    val navController = rememberNavController()
    return remember(navController) { Navigator(navController) }
}

@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.ui.search

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/** 検索画面のルート定義。 */
@Serializable
data object SearchRoute

/**
 * 検索画面のナビゲーション定義。
 *
 * - open  : 下からスライドイン（モーダル）
 * - → 詳細 : 横スライド（push）
 * - ← 詳細 : 横スライド（pop）
 * - dismiss: 下へスライドアウト（モーダル）
 */
fun NavGraphBuilder.searchScreen(
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    composable<SearchRoute>(
        enterTransition = { slideInVertically(initialOffsetY = { it }, animationSpec = tween(350)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(350)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(350)) },
        popExitTransition = { slideOutVertically(targetOffsetY = { it }, animationSpec = tween(350)) },
    ) {
        SearchScreen(
            onPokemonClick = onPokemonClick,
            onBack = onBack,
        )
    }
}

package com.yamamuto.android_sample_mvvm.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@PublishedApi
internal const val ANIM_DURATION = 350

/**
 * モーダル画面用の composable。
 *
 * - open: 下からスライドイン
 * - → 子画面: 横スライド (push)
 * - ← 子画面: 横スライド (pop)
 * - dismiss: 下へスライドアウト
 *
 * 検索・お気に入りなど、背景画面を動かしたくない画面に使う。
 */
inline fun <reified T : Any> NavGraphBuilder.modalComposable(
    noinline content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable<T>(
        enterTransition = {
            slideInVertically(initialOffsetY = { it }, animationSpec = tween(ANIM_DURATION))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(ANIM_DURATION))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(ANIM_DURATION))
        },
        popExitTransition = {
            slideOutVertically(targetOffsetY = { it }, animationSpec = tween(ANIM_DURATION))
        },
        content = content,
    )
}

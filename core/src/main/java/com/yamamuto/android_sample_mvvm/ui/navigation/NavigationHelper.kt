@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

const val ANIM_DURATION = 350

@PublishedApi
internal val modalRouteNames = mutableSetOf<String?>()

@PublishedApi
internal fun NavDestination.isModal(): Boolean = route in modalRouteNames

/**
 * 標準的な横スライドで遷移する composable。
 *
 * モーダル画面へ遷移する際は自身のアニメーションを抑制する。
 */
inline fun <reified T : Any> NavGraphBuilder.pushComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(ANIM_DURATION))
        },
        exitTransition = {
            if (targetState.destination.isModal()) {
                ExitTransition.None
            } else {
                slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(ANIM_DURATION))
            }
        },
        popEnterTransition = {
            if (initialState.destination.isModal()) {
                EnterTransition.None
            } else {
                slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(ANIM_DURATION))
            }
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(ANIM_DURATION))
        },
        content = content,
    )
}

/**
 * モーダル画面用の composable。
 *
 * - open: 下からスライドイン
 * - → 子画面: 横スライド (push)
 * - ← 子画面: 横スライド (pop)
 * - dismiss: 下へスライドアウト
 */
inline fun <reified T : Any> NavGraphBuilder.modalComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    modalRouteNames += T::class.qualifiedName
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

// ── 簡易ヘルパー: backStackEntry を隠蔽 ──────────────────────────

/** 引数なしルート用。Screen composable をそのまま渡せる。 */
inline fun <reified T : Any> NavGraphBuilder.pushScreen(noinline content: @Composable () -> Unit) {
    pushComposable<T> { content() }
}

/** 引数なしモーダルルート用。 */
inline fun <reified T : Any> NavGraphBuilder.modalScreen(noinline content: @Composable () -> Unit) {
    modalComposable<T> { content() }
}

/** 引数ありルート用。型安全な Route を直接受け取れる。 */
inline fun <reified T : Any> NavGraphBuilder.pushScreenWithRoute(noinline content: @Composable (route: T) -> Unit) {
    pushComposable<T> { backStackEntry -> content(backStackEntry.toRoute<T>()) }
}

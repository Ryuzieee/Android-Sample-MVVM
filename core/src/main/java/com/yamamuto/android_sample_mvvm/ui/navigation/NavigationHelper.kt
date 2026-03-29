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

private const val ANIM_DURATION = 350

/** モーダルルートの登録・判定を管理するオブジェクト。NavGraph 構築時に登録される。 */
@PublishedApi internal object ModalRoutes {
    private val names = mutableSetOf<String?>()

    fun register(name: String?) {
        names += name
    }

    fun isModal(destination: NavDestination): Boolean = destination.route in names
}

// ── Push トランジション ──────────────────────────

@PublishedApi
internal fun pushEnterTransition(): EnterTransition = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(ANIM_DURATION))

@PublishedApi internal fun pushExitTransition(target: NavDestination): ExitTransition =
    if (ModalRoutes.isModal(target)) {
        ExitTransition.None
    } else {
        slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(ANIM_DURATION))
    }

@PublishedApi internal fun pushPopEnterTransition(initial: NavDestination): EnterTransition =
    if (ModalRoutes.isModal(initial)) {
        EnterTransition.None
    } else {
        slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(ANIM_DURATION))
    }

@PublishedApi internal fun pushPopExitTransition(): ExitTransition =
    slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(ANIM_DURATION),
    )

// ── Modal トランジション ─────────────────────────

@PublishedApi internal fun modalEnterTransition(): EnterTransition =
    slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(ANIM_DURATION),
    )

@PublishedApi internal fun modalExitTransition(): ExitTransition =
    slideOutHorizontally(
        targetOffsetX = { -it / 3 },
        animationSpec = tween(ANIM_DURATION),
    )

@PublishedApi internal fun modalPopEnterTransition(): EnterTransition =
    slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(ANIM_DURATION))

@PublishedApi internal fun modalPopExitTransition(): ExitTransition =
    slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(ANIM_DURATION),
    )

// ── NavGraphBuilder 拡張 ─────────────────────────

/** 標準的な横スライドで遷移する composable。モーダル画面へ遷移する際は自身のアニメーションを抑制する。 */
inline fun <reified T : Any> NavGraphBuilder.pushComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        enterTransition = { pushEnterTransition() },
        exitTransition = { pushExitTransition(targetState.destination) },
        popEnterTransition = { pushPopEnterTransition(initialState.destination) },
        popExitTransition = { pushPopExitTransition() },
        content = content,
    )
}

/** モーダル画面用の composable。下からスライドインし、下へスライドアウトする。 */
inline fun <reified T : Any> NavGraphBuilder.modalComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    ModalRoutes.register(T::class.qualifiedName)
    composable<T>(
        enterTransition = { modalEnterTransition() },
        exitTransition = { modalExitTransition() },
        popEnterTransition = { modalPopEnterTransition() },
        popExitTransition = { modalPopExitTransition() },
        content = content,
    )
}

// ── 簡易ヘルパー ─────────────────────────────────

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

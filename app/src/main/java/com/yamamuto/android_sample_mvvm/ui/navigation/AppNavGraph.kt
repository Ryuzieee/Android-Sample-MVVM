@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yamamuto.android_sample_mvvm.ui.detail.PokemonDetailRoute
import com.yamamuto.android_sample_mvvm.ui.detail.pokemonDetailScreen
import com.yamamuto.android_sample_mvvm.ui.favorites.FavoritesRoute
import com.yamamuto.android_sample_mvvm.ui.favorites.favoritesScreen
import com.yamamuto.android_sample_mvvm.ui.list.PokemonListRoute
import com.yamamuto.android_sample_mvvm.ui.list.pokemonListScreen
import com.yamamuto.android_sample_mvvm.ui.search.SearchRoute
import com.yamamuto.android_sample_mvvm.ui.search.searchScreen

private val modalRoutes =
    setOf(
        SearchRoute::class.qualifiedName,
        FavoritesRoute::class.qualifiedName,
    )

private fun NavDestination.isModal(): Boolean = route in modalRoutes

/**
 * アプリ全体のナビゲーショングラフ。
 *
 * 各 feature モジュールが公開する拡張関数を組み合わせてグラフを構築する。
 * Search / Favorites はモーダル遷移のため、背景画面（一覧）はアニメーションしない。
 */
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PokemonListRoute,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350))
        },
        exitTransition = {
            // モーダル画面へ遷移するとき背景は動かさない
            if (targetState.destination.isModal()) {
                ExitTransition.None
            } else {
                slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(350))
            }
        },
        popEnterTransition = {
            // モーダル画面が閉じられるとき背景は動かさない
            if (initialState.destination.isModal()) {
                EnterTransition.None
            } else {
                slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(350))
            }
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350))
        },
    ) {
        pokemonListScreen(
            onPokemonClick = { name -> navController.navigate(PokemonDetailRoute(name)) },
            onSearchClick = { navController.navigate(SearchRoute) },
            onFavoritesClick = { navController.navigate(FavoritesRoute) },
        )
        pokemonDetailScreen(
            onBack = { navController.popBackStack() },
        )
        searchScreen(
            onPokemonClick = { name -> navController.navigate(PokemonDetailRoute(name)) },
            onBack = { navController.popBackStack() },
        )
        favoritesScreen(
            onPokemonClick = { name -> navController.navigate(PokemonDetailRoute(name)) },
            onBack = { navController.popBackStack() },
        )
    }
}

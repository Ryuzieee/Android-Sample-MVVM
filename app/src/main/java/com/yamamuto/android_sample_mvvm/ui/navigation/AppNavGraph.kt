package com.yamamuto.android_sample_mvvm.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yamamuto.android_sample_mvvm.ui.detail.PokemonDetailRoute
import com.yamamuto.android_sample_mvvm.ui.detail.pokemonDetailScreen
import com.yamamuto.android_sample_mvvm.ui.list.PokemonListRoute
import com.yamamuto.android_sample_mvvm.ui.list.pokemonListScreen

/**
 * アプリ全体のナビゲーショングラフ。
 *
 * 各 feature モジュールが公開する拡張関数を組み合わせてグラフを構築する。
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
            slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(350))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(350))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350))
        },
    ) {
        pokemonListScreen(
            onPokemonClick = { name -> navController.navigate(PokemonDetailRoute(name)) },
        )
        pokemonDetailScreen(
            onBack = { navController.popBackStack() },
        )
    }
}

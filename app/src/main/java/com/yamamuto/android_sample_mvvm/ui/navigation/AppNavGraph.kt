package com.yamamuto.android_sample_mvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yamamuto.android_sample_mvvm.ui.detail.PokemonDetailScreen
import com.yamamuto.android_sample_mvvm.ui.list.PokemonListScreen

/**
 * アプリ全体のナビゲーショングラフ。
 *
 * 画面遷移の定義と各画面へのルーティングをまとめて管理する。
 */
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "pokemon_list") {
        composable("pokemon_list") {
            PokemonListScreen(
                onPokemonClick = { name -> navController.navigate("pokemon_detail/$name") },
            )
        }
        composable("pokemon_detail/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: return@composable
            PokemonDetailScreen(
                pokemonName = name,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

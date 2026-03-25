package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yamamuto.android_sample_mvvm.ui.navigation.PokemonDetailRoute

/** ポケモン詳細画面のナビゲーション定義。 */
fun NavGraphBuilder.pokemonDetailScreen(onBack: () -> Unit) {
    composable<PokemonDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<PokemonDetailRoute>()
        PokemonDetailScreen(
            pokemonName = route.name,
            onBack = onBack,
        )
    }
}

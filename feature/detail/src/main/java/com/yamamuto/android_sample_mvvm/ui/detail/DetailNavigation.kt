@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

/** ポケモン詳細画面のルート定義。各 feature モジュールがルートを自己管理する。 */
@Serializable
data class PokemonDetailRoute(
    val name: String,
)

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

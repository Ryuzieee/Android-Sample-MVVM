@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.yamamuto.android_sample_mvvm.ui.navigation.pushComposable
import kotlinx.serialization.Serializable

/** ポケモン詳細画面のルート定義。 */
@Serializable
data class PokemonDetailRoute(
    val name: String,
)

/** ポケモン詳細画面のナビゲーション定義。 */
fun NavGraphBuilder.pokemonDetailScreen(onBack: () -> Unit) {
    pushComposable<PokemonDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<PokemonDetailRoute>()
        PokemonDetailScreen(
            pokemonName = route.name,
            onBack = onBack,
        )
    }
}

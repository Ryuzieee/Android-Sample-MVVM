package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.navigation.NavGraphBuilder
import com.yamamuto.android_sample_mvvm.ui.navigation.pushScreenWithRoute
import kotlinx.serialization.Serializable

/** ポケモン詳細画面のルート定義。 */
@Serializable
data class PokemonDetailRoute(
    val name: String,
)

/** ポケモン詳細画面のナビゲーション定義。 */
fun NavGraphBuilder.pokemonDetailScreen(onBack: () -> Unit) {
    pushScreenWithRoute<PokemonDetailRoute> { route ->
        PokemonDetailScreen(
            pokemonName = route.name,
            onBack = onBack,
        )
    }
}

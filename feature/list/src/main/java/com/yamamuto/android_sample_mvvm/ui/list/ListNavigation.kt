@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.ui.list

import androidx.navigation.NavGraphBuilder
import com.yamamuto.android_sample_mvvm.ui.navigation.pushComposable
import kotlinx.serialization.Serializable

/** ポケモン一覧画面のルート定義。 */
@Serializable
data object PokemonListRoute

/** ポケモン一覧画面のナビゲーション定義。 */
fun NavGraphBuilder.pokemonListScreen(
    onPokemonClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
) {
    pushComposable<PokemonListRoute> {
        PokemonListScreen(
            onPokemonClick = onPokemonClick,
            onSearchClick = onSearchClick,
            onFavoritesClick = onFavoritesClick,
        )
    }
}

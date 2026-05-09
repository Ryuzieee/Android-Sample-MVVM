package com.yamamuto.android_sample_mvvm.ui.list

import androidx.navigation.NavGraphBuilder
import com.yamamuto.android_sample_mvvm.ui.detail.PokemonDetailRoute
import com.yamamuto.android_sample_mvvm.ui.favorites.FavoritesRoute
import com.yamamuto.android_sample_mvvm.ui.navigation.Navigator
import com.yamamuto.android_sample_mvvm.ui.navigation.pushScreen
import com.yamamuto.android_sample_mvvm.ui.search.SearchRoute
import kotlinx.serialization.Serializable

/** ポケモン一覧画面のルート定義。 */
@Serializable
data object PokemonListRoute

/** ポケモン一覧画面のナビゲーション定義。 */
fun NavGraphBuilder.pokemonListScreen(navigator: Navigator) {
    pushScreen<PokemonListRoute> {
        PokemonListScreen(
            onPokemonClick = { name -> navigator.navigate(PokemonDetailRoute(name)) },
            onSearchClick = { navigator.navigate(SearchRoute) },
            onFavoritesClick = { navigator.navigate(FavoritesRoute) },
        )
    }
}

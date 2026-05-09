@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.yamamuto.android_sample_mvvm.ui.detail.pokemonDetailScreen
import com.yamamuto.android_sample_mvvm.ui.favorites.favoritesScreen
import com.yamamuto.android_sample_mvvm.ui.list.PokemonListRoute
import com.yamamuto.android_sample_mvvm.ui.list.pokemonListScreen
import com.yamamuto.android_sample_mvvm.ui.search.searchScreen

/** アプリ全体のナビゲーショングラフ。 */
@Composable
fun AppNavGraph() {
    val navigator = rememberAppNavigator()

    NavHost(
        navController = navigator.navController,
        startDestination = PokemonListRoute,
    ) {
        pokemonListScreen(navigator)
        pokemonDetailScreen(navigator)
        searchScreen(navigator)
        favoritesScreen(navigator)
    }
}

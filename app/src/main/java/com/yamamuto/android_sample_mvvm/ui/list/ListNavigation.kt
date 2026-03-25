package com.yamamuto.android_sample_mvvm.ui.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yamamuto.android_sample_mvvm.ui.navigation.PokemonListRoute

/** ポケモン一覧画面のナビゲーション定義。 */
fun NavGraphBuilder.pokemonListScreen(onPokemonClick: (String) -> Unit) {
    composable<PokemonListRoute> {
        PokemonListScreen(onPokemonClick = onPokemonClick)
    }
}

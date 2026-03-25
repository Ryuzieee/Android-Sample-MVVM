package com.yamamuto.android_sample_mvvm.ui.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/** ポケモン一覧画面のルート定義。各 feature モジュールがルートを自己管理する。 */
@Serializable
data object PokemonListRoute

/** ポケモン一覧画面のナビゲーション定義。 */
fun NavGraphBuilder.pokemonListScreen(onPokemonClick: (String) -> Unit) {
    composable<PokemonListRoute> {
        PokemonListScreen(onPokemonClick = onPokemonClick)
    }
}

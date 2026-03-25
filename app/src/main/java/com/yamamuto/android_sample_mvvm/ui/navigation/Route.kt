package com.yamamuto.android_sample_mvvm.ui.navigation

import kotlinx.serialization.Serializable

/** ポケモン一覧画面のルート定義。 */
@Serializable
data object PokemonListRoute

/** ポケモン詳細画面のルート定義。 */
@Serializable
data class PokemonDetailRoute(val name: String)

package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.navigation.NavGraphBuilder
import com.yamamuto.android_sample_mvvm.ui.detail.PokemonDetailRoute
import com.yamamuto.android_sample_mvvm.ui.navigation.Navigator
import com.yamamuto.android_sample_mvvm.ui.navigation.modalScreen
import kotlinx.serialization.Serializable

/** お気に入り一覧画面のルート定義。 */
@Serializable
data object FavoritesRoute

/** お気に入り一覧画面のナビゲーション定義。モーダル遷移を適用。 */
fun NavGraphBuilder.favoritesScreen(navigator: Navigator) {
    modalScreen<FavoritesRoute> {
        FavoritesScreen(
            onPokemonClick = { name -> navigator.navigate(PokemonDetailRoute(name)) },
            onBack = navigator::back,
        )
    }
}

package com.yamamuto.android_sample_mvvm.ui.search

import androidx.navigation.NavGraphBuilder
import com.yamamuto.android_sample_mvvm.ui.detail.PokemonDetailRoute
import com.yamamuto.android_sample_mvvm.ui.navigation.Navigator
import com.yamamuto.android_sample_mvvm.ui.navigation.modalScreen
import kotlinx.serialization.Serializable

/** 検索画面のルート定義。 */
@Serializable
data object SearchRoute

/** 検索画面のナビゲーション定義。モーダル遷移を適用。 */
fun NavGraphBuilder.searchScreen(navigator: Navigator) {
    modalScreen<SearchRoute> {
        SearchScreen(
            onPokemonClick = { name -> navigator.navigate(PokemonDetailRoute(name)) },
            onBack = navigator::back,
        )
    }
}

package com.yamamuto.android_sample_mvvm.ui.search

import androidx.navigation.NavGraphBuilder
import com.yamamuto.android_sample_mvvm.ui.navigation.modalScreen
import kotlinx.serialization.Serializable

/** 検索画面のルート定義。 */
@Serializable
data object SearchRoute

/** 検索画面のナビゲーション定義。モーダル遷移を適用。 */
fun NavGraphBuilder.searchScreen(
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    modalScreen<SearchRoute> {
        SearchScreen(
            onPokemonClick = onPokemonClick,
            onBack = onBack,
        )
    }
}

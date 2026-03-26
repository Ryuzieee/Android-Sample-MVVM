@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.navigation.NavGraphBuilder
import com.yamamuto.android_sample_mvvm.ui.navigation.modalComposable
import kotlinx.serialization.Serializable

/** お気に入り一覧画面のルート定義。 */
@Serializable
data object FavoritesRoute

/** お気に入り一覧画面のナビゲーション定義。モーダル遷移を適用。 */
fun NavGraphBuilder.favoritesScreen(
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    modalComposable<FavoritesRoute> {
        FavoritesScreen(
            onPokemonClick = onPokemonClick,
            onBack = onBack,
        )
    }
}

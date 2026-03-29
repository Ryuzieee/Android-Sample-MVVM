package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.ui.Strings
import com.yamamuto.android_sample_mvvm.ui.component.AppPullRefresh
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.EmptyContent
import com.yamamuto.android_sample_mvvm.ui.component.PokemonGrid
import com.yamamuto.android_sample_mvvm.ui.component.PokemonGridItem
import com.yamamuto.android_sample_mvvm.ui.component.UiStateContent

@Composable
fun FavoritesScreen(
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        title = { Text(Strings.Favorites.SCREEN_TITLE) },
        onBack = onBack,
    ) { padding ->
        AppPullRefresh(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.padding(padding),
        ) {
            UiStateContent(
                state = uiState.content,
                onRetry = viewModel::retry,
            ) { favorites ->
                if (favorites.isEmpty()) {
                    FavoritesEmpty()
                } else {
                    PokemonGrid(
                        items = favorites.map { it.toGridItem() },
                        onPokemonClick = onPokemonClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesEmpty() {
    EmptyContent(
        message = Strings.Favorites.EMPTY_MESSAGE,
        subMessage = Strings.Favorites.EMPTY_SUB_MESSAGE,
    )
}

private fun FavoriteModel.toGridItem() =
    PokemonGridItem(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )

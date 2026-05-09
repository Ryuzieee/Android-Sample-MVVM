package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.core.R
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
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
        title = { Text(stringResource(R.string.favorites_screen_title)) },
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
        message = stringResource(R.string.favorites_empty_message),
        subMessage = stringResource(R.string.favorites_empty_sub_message),
    )
}

private fun FavoriteModel.toGridItem() =
    PokemonGridItem(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )

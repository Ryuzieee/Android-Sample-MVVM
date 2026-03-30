package com.yamamuto.android_sample_mvvm.ui.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.ui.Strings
import com.yamamuto.android_sample_mvvm.ui.component.AppIconButton
import com.yamamuto.android_sample_mvvm.ui.component.AppLazyVerticalGrid
import com.yamamuto.android_sample_mvvm.ui.component.AppPullRefresh
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.PokemonCard
import com.yamamuto.android_sample_mvvm.ui.component.UiStateContent

@Composable
fun PokemonListScreen(
    onPokemonClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        title = { Text(Strings.List.SCREEN_TITLE) },
        actions = {
            AppIconButton(
                Icons.Filled.Search,
                Strings.List.SEARCH_DESCRIPTION,
                onClick = onSearchClick,
            )
            AppIconButton(
                Icons.Filled.Favorite,
                Strings.List.FAVORITES_DESCRIPTION,
                onClick = onFavoritesClick,
            )
        },
    ) { padding ->
        UiStateContent(
            state = uiState.loadState,
            onRetry = viewModel::refresh,
            modifier = Modifier.padding(padding),
        ) {
            AppPullRefresh(
                isRefreshing = uiState.isRefreshing,
                onRefresh = viewModel::refresh,
            ) {
                AppLazyVerticalGrid(
                    contentPadding = padding,
                    onLoadMore = viewModel::loadMore,
                    isLoadingMore = uiState.isLoadingMore,
                ) {
                    items(uiState.items.size) { index ->
                        val pokemon = uiState.items[index]
                        PokemonCard(
                            name = pokemon.name,
                            id = pokemon.id,
                            imageUrl = pokemon.imageUrl,
                            onClick = { onPokemonClick(pokemon.name) },
                        )
                    }
                }
            }
        }
    }
}

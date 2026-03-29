package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.ui.Strings
import com.yamamuto.android_sample_mvvm.ui.component.AppLazyVerticalGrid
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.EmptyContent
import com.yamamuto.android_sample_mvvm.ui.component.PokemonCard
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
        UiStateContent(
            state = uiState,
            modifier = Modifier.padding(padding),
        ) { favorites ->
            if (favorites.isEmpty()) {
                FavoritesEmpty(padding)
            } else {
                FavoritesContent(favorites, onPokemonClick, padding)
            }
        }
    }
}

@Composable
private fun FavoritesEmpty(padding: PaddingValues) {
    EmptyContent(
        message = Strings.Favorites.EMPTY_MESSAGE,
        subMessage = Strings.Favorites.EMPTY_SUB_MESSAGE,
        modifier = Modifier.padding(padding),
    )
}

@Composable
private fun FavoritesContent(
    favorites: List<FavoriteModel>,
    onPokemonClick: (String) -> Unit,
    padding: PaddingValues,
) {
    AppLazyVerticalGrid(contentPadding = padding) {
        items(favorites, key = { it.id }) { favorite ->
            PokemonCard(
                name = favorite.name,
                id = favorite.id,
                imageUrl = favorite.imageUrl,
                onClick = { onPokemonClick(favorite.name) },
            )
        }
    }
}

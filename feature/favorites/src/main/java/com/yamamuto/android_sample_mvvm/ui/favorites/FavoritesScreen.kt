package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.domain.model.Favorite
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
        title = { Text("お気に入り") },
        onBack = onBack,
    ) { padding ->
        UiStateContent(
            state = uiState.contentState,
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
        message = "お気に入りがありません",
        subMessage = "詳細画面のハートアイコンから追加できます",
        modifier = Modifier.padding(padding),
    )
}

@Composable
private fun FavoritesContent(
    favorites: List<Favorite>,
    onPokemonClick: (String) -> Unit,
    padding: PaddingValues,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = padding,
    ) {
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

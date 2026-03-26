package com.yamamuto.android_sample_mvvm.ui.favorites

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
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.EmptyContent
import com.yamamuto.android_sample_mvvm.ui.component.PokemonCard

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
        if (uiState.favorites.isEmpty()) {
            EmptyContent(
                message = "お気に入りがありません",
                subMessage = "詳細画面のハートアイコンから追加できます",
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = padding,
            ) {
                items(uiState.favorites, key = { it.id }) { favorite ->
                    PokemonCard(
                        name = favorite.name,
                        id = favorite.id,
                        imageUrl = favorite.imageUrl,
                        onClick = { onPokemonClick(favorite.name) },
                    )
                }
            }
        }
    }
}

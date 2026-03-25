package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.yamamuto.android_sample_mvvm.domain.model.Favorite
import com.yamamuto.android_sample_mvvm.ui.component.PokemonIdText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonNameText

/**
 * お気に入り一覧画面。
 *
 * Room に保存されたお気に入りポケモンをグリッド形式で表示する。
 * お気に入りが空の場合は空状態メッセージを表示する。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("お気に入り") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        if (favorites.isEmpty()) {
            FavoritesEmptyContent(modifier = Modifier.padding(padding))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = padding,
            ) {
                items(favorites, key = { it.id }) { favorite ->
                    FavoriteCard(
                        favorite = favorite,
                        onClick = { onPokemonClick(favorite.name) },
                    )
                }
            }
        }
    }
}

/** お気に入りが空のときに表示するコンテンツ。 */
@Composable
private fun FavoritesEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "お気に入りがありません",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = "詳細画面のハートアイコンから追加できます",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

/** お気に入りの各カードコンポーネント。 */
@Composable
private fun FavoriteCard(
    favorite: Favorite,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
        ) {
            AsyncImage(
                model = favorite.imageUrl,
                contentDescription = favorite.name,
                modifier = Modifier.size(80.dp),
            )
            PokemonNameText(
                name = favorite.name,
                modifier = Modifier.padding(top = 4.dp),
            )
            PokemonIdText(id = favorite.id)
        }
    }
}

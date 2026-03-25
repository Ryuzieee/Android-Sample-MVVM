package com.yamamuto.android_sample_mvvm.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.ui.component.ErrorContent
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator
import com.yamamuto.android_sample_mvvm.ui.component.PokemonIdText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonNameText

/**
 * ポケモン一覧画面。
 *
 * PokeAPI から取得したポケモンをグリッド形式で表示する。
 * Paging 3 による無限スクロールに対応。TopAppBar から検索・お気に入りへ遷移できる。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = uiState.pagingData.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Filled.Search, contentDescription = "検索")
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Filled.Favorite, contentDescription = "お気に入り")
                    }
                },
            )
        },
    ) { padding ->
        when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> LoadingIndicator()

            is LoadState.Error -> {
                val error = (pagingItems.loadState.refresh as LoadState.Error).error
                ErrorContent(
                    message = error.message ?: "Unknown error",
                    onRetry = { pagingItems.retry() },
                    modifier = Modifier.padding(padding),
                )
            }

            is LoadState.NotLoading ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = padding,
                ) {
                    items(pagingItems.itemCount) { index ->
                        pagingItems[index]?.let { pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                onClick = { onPokemonClick(pokemon.name) },
                            )
                        }
                    }

                    if (pagingItems.loadState.append is LoadState.Loading) {
                        item { LoadingIndicator() }
                    }
                }
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: Pokemon,
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
                    .fillMaxSize()
                    .padding(12.dp),
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(80.dp),
            )
            PokemonNameText(
                name = pokemon.name,
                modifier = Modifier.padding(top = 4.dp),
            )
            PokemonIdText(id = pokemon.id)
        }
    }
}

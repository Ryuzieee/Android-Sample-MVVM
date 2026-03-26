package com.yamamuto.android_sample_mvvm.ui.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.ErrorContent
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator
import com.yamamuto.android_sample_mvvm.ui.component.PokemonCard

@Composable
fun PokemonListScreen(
    onPokemonClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = uiState.pagingData.collectAsLazyPagingItems()

    AppScaffold(
        title = { Text("Pokédex") },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Filled.Search, contentDescription = "検索")
            }
            IconButton(onClick = onFavoritesClick) {
                Icon(Icons.Filled.Favorite, contentDescription = "お気に入り")
            }
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
                                name = pokemon.name,
                                id = pokemon.id,
                                imageUrl = pokemon.imageUrl,
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

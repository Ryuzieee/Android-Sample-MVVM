package com.yamamuto.android_sample_mvvm.ui.list

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator
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
        val refreshState = pagingItems.loadState.refresh
        UiStateContent(
            state = when (refreshState) {
                is LoadState.Loading -> UiState.Loading
                is LoadState.Error -> UiState.Error(
                    message = refreshState.error.message ?: "Unknown error",
                    isNetworkError = refreshState.error is AppException.Network,
                )
                is LoadState.NotLoading -> UiState.Success(pagingItems)
            },
            onRetry = { pagingItems.retry() },
            modifier = Modifier.padding(padding),
        ) { items ->
            PokemonListContent(items, padding, onPokemonClick)
        }
    }
}

@Composable
private fun PokemonListContent(
    pagingItems: LazyPagingItems<Pokemon>,
    padding: PaddingValues,
    onPokemonClick: (String) -> Unit,
) {
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

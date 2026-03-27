package com.yamamuto.android_sample_mvvm.ui.list

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.ui.component.AppIconButton
import com.yamamuto.android_sample_mvvm.ui.component.AppLazyVerticalGrid
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator
import com.yamamuto.android_sample_mvvm.ui.component.PagingContent
import com.yamamuto.android_sample_mvvm.ui.component.PokemonCard

@Composable
fun PokemonListScreen(
    onPokemonClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        title = { Text("Pokédex") },
        actions = {
            AppIconButton(Icons.Filled.Search, "検索", onClick = onSearchClick)
            AppIconButton(Icons.Filled.Favorite, "お気に入り", onClick = onFavoritesClick)
        },
    ) { padding ->
        PagingContent(
            pagingData = uiState.pagingData,
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
    AppLazyVerticalGrid(contentPadding = padding) {
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

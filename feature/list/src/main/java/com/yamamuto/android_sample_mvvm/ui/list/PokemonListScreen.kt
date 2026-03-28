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
import com.yamamuto.android_sample_mvvm.ui.component.AppIconButton
import com.yamamuto.android_sample_mvvm.ui.component.AppLazyVerticalGrid
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator
import com.yamamuto.android_sample_mvvm.ui.component.PagingContent
import com.yamamuto.android_sample_mvvm.ui.component.PokemonCard
import com.yamamuto.android_sample_mvvm.ui.component.isAppendLoading

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
        ) { pagingItems ->
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
                if (pagingItems.isAppendLoading) {
                    item { LoadingIndicator() }
                }
            }
        }
    }
}

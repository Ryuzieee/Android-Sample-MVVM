package com.yamamuto.android_sample_mvvm.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.ui.component.ErrorContent
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator

/**
 * ポケモン一覧画面。
 *
 * PokeAPI から取得したポケモンをグリッド形式で表示する。
 * Paging 3 による無限スクロールに対応。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonClick: (String) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val pagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pokédex") }) },
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
                        item {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                androidx.compose.material3.CircularProgressIndicator()
                            }
                        }
                    }
                }
        }
    }
}

/** ポケモン一覧の各カードコンポーネント。 */
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
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                text = "#${pokemon.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

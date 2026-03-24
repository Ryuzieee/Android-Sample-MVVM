package com.yamamuto.android_sample_mvvm.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator

/**
 * ポケモン一覧画面。
 *
 * PokeAPI から取得したポケモンをグリッド形式で表示する。
 * タップすると詳細画面へ遷移する。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonClick: (String) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pokédex") }) },
    ) { padding ->
        when (val state = viewModel.uiState) {
            is PokemonListUiState.Loading -> LoadingIndicator()

            is PokemonListUiState.Error -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Error: ${state.message}")
            }

            is PokemonListUiState.Success -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = padding,
            ) {
                items(state.pokemons) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        onClick = { onPokemonClick(pokemon.name) },
                    )
                }
            }
        }
    }
}

/** ポケモン一覧の各カードコンポーネント。 */
@Composable
private fun PokemonCard(pokemon: Pokemon, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
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

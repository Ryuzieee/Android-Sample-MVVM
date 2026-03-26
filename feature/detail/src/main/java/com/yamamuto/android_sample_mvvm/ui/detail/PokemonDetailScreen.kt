package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.yamamuto.android_sample_mvvm.ui.component.AppText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.PokemonIdText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonImage
import com.yamamuto.android_sample_mvvm.ui.component.PokemonNameText
import com.yamamuto.android_sample_mvvm.ui.component.UiStateContent
import com.yamamuto.android_sample_mvvm.ui.util.ObserveAsEvents
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import kotlinx.coroutines.launch

@Composable
fun PokemonDetailScreen(
    pokemonName: String,
    onBack: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is UiEvent.ShowSnackbar -> scope.launch { snackbarHostState.showSnackbar(event.message) }
            is UiEvent.NavigateBack -> onBack()
        }
    }

    AppScaffold(
        title = { PokemonNameText(name = pokemonName, style = MaterialTheme.typography.titleLarge) },
        onBack = onBack,
        actions = {
            IconButton(onClick = viewModel::toggleFavorite) {
                Icon(
                    imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (uiState.isFavorite) "お気に入りから削除" else "お気に入りに追加",
                    tint = if (uiState.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        snackbarHostState = snackbarHostState,
    ) { padding ->
        UiStateContent(
            state = uiState.contentState,
            onRetry = viewModel::retry,
            modifier = Modifier.padding(padding),
        ) { detail ->
            PokemonDetailContent(detail = detail)
        }
    }
}

@Composable
private fun PokemonDetailContent(
    detail: PokemonDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PokemonImage(
            imageUrl = detail.imageUrl,
            contentDescription = detail.name,
        )

        PokemonIdText(id = detail.id)
        PokemonNameText(
            name = detail.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 4.dp),
        )

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            detail.types.forEach { type ->
                AssistChip(
                    onClick = {},
                    label = { AppText(type) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }

        AppText(
            text = "Height: ${detail.height * 10} cm  ·  Weight: ${detail.weight / 10.0} kg",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        AppText(
            text = "Base Stats",
            style = MaterialTheme.typography.titleMedium,
            bold = true,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp),
        )

        detail.stats.forEach { stat ->
            StatRow(stat = stat)
        }
    }
}

@Composable
private fun StatRow(
    stat: PokemonDetail.Stat,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(
            text = stat.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(110.dp),
        )
        LinearProgressIndicator(
            progress = { stat.value / 255f },
            modifier = Modifier.weight(1f),
        )
        AppText(
            text = "${stat.value}",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End,
            modifier = Modifier.width(36.dp),
        )
    }
}

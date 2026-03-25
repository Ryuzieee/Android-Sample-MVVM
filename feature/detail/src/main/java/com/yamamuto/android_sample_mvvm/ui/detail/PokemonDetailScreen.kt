package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.ui.component.ErrorContent
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator
import com.yamamuto.android_sample_mvvm.ui.component.PokemonIdText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonNameText
import com.yamamuto.android_sample_mvvm.ui.util.ObserveAsEvents
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import kotlinx.coroutines.launch

/**
 * ポケモン詳細画面。
 *
 * 公式アートワーク・タイプ・基本ステータスを表示する。
 * TopAppBar のハートアイコンでお気に入りトグルが可能。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonName: String,
    onBack: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is UiEvent.ShowSnackbar -> scope.launch { snackbarHostState.showSnackbar(event.message) }
            is UiEvent.NavigateBack -> onBack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    PokemonNameText(
                        name = pokemonName,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::toggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isFavorite) "お気に入りから削除" else "お気に入りに追加",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
    ) { padding ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        when (val state = uiState) {
            is UiState.Loading -> LoadingIndicator()

            is UiState.Error ->
                ErrorContent(
                    message = state.message,
                    onRetry = viewModel::retry,
                    isNetworkError = state.isNetworkError,
                    modifier = Modifier.padding(padding),
                )

            is UiState.Success ->
                PokemonDetailContent(
                    detail = state.data,
                    modifier = Modifier.padding(padding),
                )
        }
    }
}

/** ポケモン詳細のコンテンツ部分。スクロール可能なレイアウトで情報を縦並びに表示する。 */
@Composable
private fun PokemonDetailContent(
    detail: PokemonDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = detail.imageUrl,
            contentDescription = detail.name,
            modifier =
                Modifier
                    .size(200.dp)
                    .padding(top = 16.dp),
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
                    label = { Text(type) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }

        Text(
            text = "Height: ${detail.height * 10} cm  ·  Weight: ${detail.weight / 10.0} kg",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Text(
            text = "Base Stats",
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp, bottom = 8.dp),
        )

        detail.stats.forEach { stat ->
            StatRow(stat = stat)
        }
    }
}

/** ステータス名・プログレスバー・数値を横並びで表示する行コンポーネント。 */
@Composable
private fun StatRow(
    stat: PokemonDetail.Stat,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stat.name,
            modifier = Modifier.width(110.dp),
            style = MaterialTheme.typography.bodySmall,
        )
        LinearProgressIndicator(
            progress = { stat.value / 255f },
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "${stat.value}",
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

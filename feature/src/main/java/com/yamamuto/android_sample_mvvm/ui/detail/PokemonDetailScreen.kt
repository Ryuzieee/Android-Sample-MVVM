package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.ui.Strings
import com.yamamuto.android_sample_mvvm.ui.component.AppIconButton
import com.yamamuto.android_sample_mvvm.ui.component.AppPullRefresh
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.AppText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonIdText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonImage
import com.yamamuto.android_sample_mvvm.ui.component.UiStateContent
import com.yamamuto.android_sample_mvvm.ui.util.JapaneseTranslation
import com.yamamuto.android_sample_mvvm.ui.util.getOrNull

@Composable
fun PokemonDetailScreen(
    pokemonName: String,
    onBack: () -> Unit,
    onPokemonClick: (String) -> Unit = {},
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showInfo by rememberSaveable { mutableStateOf(false) }

    val fullDetail = uiState.content.getOrNull()
    val displayName = fullDetail?.species?.japaneseName?.ifEmpty { null } ?: pokemonName

    AppScaffold(
        title = {
            AppText(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onBack = onBack,
        actions = {
            AppIconButton(
                imageVector = Icons.Filled.Info,
                contentDescription = Strings.Detail.INFO_BUTTON_DESCRIPTION,
                onClick = { showInfo = true },
            )
            AppIconButton(
                imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription =
                    if (uiState.isFavorite) {
                        Strings.Detail.REMOVE_FAVORITE_DESCRIPTION
                    } else {
                        Strings.Detail.ADD_FAVORITE_DESCRIPTION
                    },
                onClick = viewModel::toggleFavorite,
                tint = if (uiState.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
        },
    ) { padding ->
        AppPullRefresh(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.padding(padding),
        ) {
            UiStateContent(
                state = uiState.content,
                onRetry = viewModel::retry,
            ) { content ->
                PokemonDetailContent(
                    detail = content.detail,
                    species = content.species,
                    evolutionChain = content.evolutionChain,
                    onEvolutionClick = onPokemonClick,
                )
            }
        }
    }

    if (showInfo && fullDetail != null) {
        PokemonInfoBottomSheet(
            detail = fullDetail.detail,
            species = fullDetail.species,
            onDismiss = { showInfo = false },
        )
    }
}

@Composable
private fun PokemonDetailContent(
    detail: PokemonDetailModel,
    species: PokemonSpeciesModel?,
    evolutionChain: List<EvolutionStageModel>,
    onEvolutionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayName = species?.japaneseName?.ifEmpty { null } ?: detail.name

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PokemonImage(
            imageUrl = detail.imageUrl,
            contentDescription = displayName,
        )

        PokemonIdText(id = detail.id)
        AppText(
            text = displayName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 4.dp),
        )

        if (species != null) {
            AppText(
                text = species.genus,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            detail.types.forEach { type ->
                AssistChip(
                    onClick = {},
                    label = { AppText(JapaneseTranslation.type(type)) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }

        AppText(
            text = Strings.Detail.heightWeight(detail.height * 10, detail.weight / 10.0),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        if (evolutionChain.size > 1) {
            AppText(
                text = Strings.Detail.SECTION_EVOLUTION,
                style = MaterialTheme.typography.titleMedium,
                bold = true,
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, bottom = 8.dp),
            )
            EvolutionChainRow(
                stages = evolutionChain,
                currentName = detail.name,
                onStageClick = onEvolutionClick,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        AppText(
            text = Strings.Detail.SECTION_BASE_STATS,
            style = MaterialTheme.typography.titleMedium,
            bold = true,
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

@Composable
private fun StatRow(
    stat: PokemonDetailModel.Stat,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(
            text = JapaneseTranslation.stat(stat.name),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(80.dp),
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

package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStage
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpecies
import com.yamamuto.android_sample_mvvm.ui.component.AppBottomSheet
import com.yamamuto.android_sample_mvvm.ui.component.AppIconButton
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.AppText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonIdText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonImage
import com.yamamuto.android_sample_mvvm.ui.component.PokemonNameText
import com.yamamuto.android_sample_mvvm.ui.component.UiStateContent
import com.yamamuto.android_sample_mvvm.ui.util.ObserveAsEvents
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import com.yamamuto.android_sample_mvvm.ui.util.getOrNull
import kotlinx.coroutines.launch

@Composable
fun PokemonDetailScreen(
    pokemonName: String,
    onBack: () -> Unit,
    onPokemonClick: (String) -> Unit = {},
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showInfo by rememberSaveable { mutableStateOf(false) }

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
            AppIconButton(
                imageVector = Icons.Filled.Info,
                contentDescription = "詳細情報を表示",
                onClick = { showInfo = true },
            )
            AppIconButton(
                imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (uiState.isFavorite) "お気に入りから削除" else "お気に入りに追加",
                onClick = viewModel::toggleFavorite,
                tint = if (uiState.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
        },
        snackbarHostState = snackbarHostState,
    ) { padding ->
        UiStateContent(
            state = uiState.contentState,
            onRetry = viewModel::retry,
            modifier = Modifier.padding(padding),
        ) { detail ->
            PokemonDetailContent(
                detail = detail,
                species = uiState.species,
                evolutionChain = uiState.evolutionChain,
                onEvolutionClick = onPokemonClick,
            )
        }
    }

    if (showInfo) {
        val detail = uiState.contentState.getOrNull()
        if (detail != null) {
            InfoBottomSheet(
                detail = detail,
                species = uiState.species,
                onDismiss = { showInfo = false },
            )
        }
    }
}

@Composable
private fun InfoBottomSheet(
    detail: PokemonDetail,
    species: PokemonSpecies?,
    onDismiss: () -> Unit,
) {
    AppBottomSheet(
        onDismiss = onDismiss,
        title = "Details",
    ) {
        // 図鑑テキスト
        if (species != null) {
            AppText(
                text = species.flavorText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Species info
            InfoRow("分類", species.genus)
            InfoRow("世代", species.generation.removePrefix("generation-").uppercase())
            species.habitat?.let { InfoRow("生息地", it) }
            InfoRow("捕獲率", "${species.captureRate}")
            InfoRow("タマゴグループ", species.eggGroups.joinToString(", "))
            val genderText = if (species.genderRate == -1) "性別なし"
            else "♀ ${species.genderRate * 12.5}% / ♂ ${(8 - species.genderRate) * 12.5}%"
            InfoRow("性別比率", genderText)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        // Abilities
        AppText(
            text = "Abilities",
            style = MaterialTheme.typography.titleSmall,
            bold = true,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        detail.abilities.forEach { ability ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PokemonNameText(
                    name = ability.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
                if (ability.isHidden) {
                    AssistChip(
                        onClick = {},
                        label = { AppText("Hidden") },
                    )
                }
            }
        }
        InfoRow("Base Experience", "${detail.baseExperience}")
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
    ) {
        AppText(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(120.dp),
        )
        AppText(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun PokemonDetailContent(
    detail: PokemonDetail,
    species: PokemonSpecies?,
    evolutionChain: List<EvolutionStage>,
    onEvolutionClick: (String) -> Unit,
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

        // 図鑑テキスト（ひとこと）
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

        // 進化チェーン
        if (evolutionChain.size > 1) {
            AppText(
                text = "Evolution",
                style = MaterialTheme.typography.titleMedium,
                bold = true,
                modifier = Modifier
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
private fun EvolutionChainRow(
    stages: List<EvolutionStage>,
    currentName: String,
    onStageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(stages) { stage ->
            if (stage != stages.first()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppText(
                        text = "→",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    if (stage.minLevel != null) {
                        AppText(
                            text = "Lv.${stage.minLevel}",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
            EvolutionStageItem(
                stage = stage,
                isCurrent = stage.name == currentName,
                onClick = { if (stage.name != currentName) onStageClick(stage.name) },
            )
        }
    }
}

@Composable
private fun EvolutionStageItem(
    stage: EvolutionStage,
    isCurrent: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable(enabled = !isCurrent, onClick = onClick)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = stage.imageUrl,
            contentDescription = stage.name,
            modifier = Modifier.size(80.dp),
            alpha = if (isCurrent) 1f else 0.6f,
        )
        PokemonNameText(
            name = stage.name,
            style = if (isCurrent) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
        )
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

package com.yamamuto.android_sample_mvvm.ui.detail.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yamamuto.android_sample_mvvm.core.R
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.translation.PokemonTermLocalizer
import com.yamamuto.android_sample_mvvm.ui.component.AppText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonIdText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonImage

@Composable
internal fun PokemonDetailContent(
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
                    label = { AppText(PokemonTermLocalizer.type(type)) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }

        AppText(
            text =
                stringResource(
                    R.string.detail_height_weight_format,
                    detail.height * 10,
                    detail.weight / 10.0,
                ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        if (evolutionChain.size > 1) {
            AppText(
                text = stringResource(R.string.detail_section_evolution),
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
            text = stringResource(R.string.detail_section_base_stats),
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
            text = PokemonTermLocalizer.stat(stat.name),
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

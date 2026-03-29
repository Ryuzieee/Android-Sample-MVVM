package com.yamamuto.android_sample_mvvm.ui.detail.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.ui.Strings
import com.yamamuto.android_sample_mvvm.ui.component.AppText

@Composable
internal fun EvolutionChainRow(
    stages: List<EvolutionStageModel>,
    currentName: String,
    onStageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(stages) { stage ->
            if (stage != stages.first()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppText(
                        text = Strings.Detail.EVOLUTION_ARROW,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    if (stage.minLevel != null) {
                        AppText(
                            text = "${Strings.Detail.EVOLUTION_LEVEL_PREFIX}${stage.minLevel}",
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
    stage: EvolutionStageModel,
    isCurrent: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayName = stage.japaneseName.ifEmpty { stage.name }
    Column(
        modifier =
            modifier
                .clickable(enabled = !isCurrent, onClick = onClick)
                .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = stage.imageUrl,
            contentDescription = displayName,
            modifier = Modifier.size(80.dp),
            alpha = if (isCurrent) 1f else 0.6f,
        )
        AppText(
            text = displayName,
            style = if (isCurrent) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
        )
    }
}

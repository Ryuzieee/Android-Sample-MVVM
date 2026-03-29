package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.ui.Strings
import com.yamamuto.android_sample_mvvm.ui.component.AppBottomSheet
import com.yamamuto.android_sample_mvvm.ui.component.AppText
import com.yamamuto.android_sample_mvvm.ui.util.JapaneseTranslation

@Composable
internal fun PokemonInfoBottomSheet(
    detail: PokemonDetailModel,
    species: PokemonSpeciesModel?,
    onDismiss: () -> Unit,
) {
    AppBottomSheet(
        onDismiss = onDismiss,
        title = Strings.Detail.BOTTOM_SHEET_TITLE,
    ) {
        if (species != null) {
            AppText(
                text = species.flavorText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            InfoRow(Strings.Detail.LABEL_CATEGORY, species.genus)
            InfoRow(
                Strings.Detail.LABEL_GENERATION,
                JapaneseTranslation.generation(species.generation),
            )
            species.habitat?.let {
                InfoRow(
                    Strings.Detail.LABEL_HABITAT,
                    JapaneseTranslation.habitat(it),
                )
            }
            InfoRow(Strings.Detail.LABEL_CAPTURE_RATE, "${species.captureRate}")
            InfoRow(
                Strings.Detail.LABEL_EGG_GROUP,
                species.eggGroups.joinToString(Strings.Detail.EGG_GROUP_SEPARATOR) {
                    JapaneseTranslation.eggGroup(it)
                },
            )
            val genderText =
                if (species.genderRate == -1) {
                    Strings.Detail.LABEL_NO_GENDER
                } else {
                    Strings.Detail.genderRatio(species.genderRate * 12.5, (8 - species.genderRate) * 12.5)
                }
            InfoRow(Strings.Detail.LABEL_GENDER_RATIO, genderText)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        AppText(
            text = Strings.Detail.LABEL_ABILITIES,
            style = MaterialTheme.typography.titleSmall,
            bold = true,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        detail.abilities.forEach { ability ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppText(
                    text = ability.japaneseName.ifEmpty { ability.name },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
                if (ability.isHidden) {
                    AssistChip(
                        onClick = {},
                        label = { AppText(Strings.Detail.LABEL_HIDDEN_ABILITY) },
                    )
                }
            }
        }
        InfoRow(Strings.Detail.LABEL_BASE_EXPERIENCE, "${detail.baseExperience}")
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
        modifier =
            modifier
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

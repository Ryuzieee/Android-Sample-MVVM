package com.yamamuto.android_sample_mvvm.ui.detail.view

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yamamuto.android_sample_mvvm.core.R
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.translation.PokemonTermLocalizer
import com.yamamuto.android_sample_mvvm.ui.component.AppBottomSheet
import com.yamamuto.android_sample_mvvm.ui.component.AppText

@Composable
internal fun PokemonInfoBottomSheet(
    detail: PokemonDetailModel,
    species: PokemonSpeciesModel?,
    onDismiss: () -> Unit,
) {
    AppBottomSheet(
        onDismiss = onDismiss,
        title = stringResource(R.string.detail_bottom_sheet_title),
    ) {
        if (species != null) {
            AppText(
                text = species.flavorText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            InfoRow(stringResource(R.string.detail_label_category), species.genus)
            InfoRow(
                stringResource(R.string.detail_label_generation),
                PokemonTermLocalizer.generation(species.generation),
            )
            species.habitat?.let {
                InfoRow(
                    stringResource(R.string.detail_label_habitat),
                    PokemonTermLocalizer.habitat(it),
                )
            }
            InfoRow(stringResource(R.string.detail_label_capture_rate), "${species.captureRate}")
            val separator = stringResource(R.string.detail_egg_group_separator)
            InfoRow(
                stringResource(R.string.detail_label_egg_group),
                species.eggGroups.joinToString(separator) {
                    PokemonTermLocalizer.eggGroup(it)
                },
            )
            val genderText =
                if (species.genderRate == -1) {
                    stringResource(R.string.detail_label_no_gender)
                } else {
                    stringResource(
                        R.string.detail_gender_ratio_format,
                        species.genderRate * 12.5,
                        (8 - species.genderRate) * 12.5,
                    )
                }
            InfoRow(stringResource(R.string.detail_label_gender_ratio), genderText)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }

        AppText(
            text = stringResource(R.string.detail_label_abilities),
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
                        label = { AppText(stringResource(R.string.detail_label_hidden_ability)) },
                    )
                }
            }
        }
        InfoRow(stringResource(R.string.detail_label_base_experience), "${detail.baseExperience}")
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

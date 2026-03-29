package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamamuto.android_sample_mvvm.network.MockScenario
import com.yamamuto.android_sample_mvvm.network.MockScenarioHolder

private val scenarioLabels =
    mapOf(
        MockScenario.SUCCESS to "正常系レスポンス",
        MockScenario.SESSION_EXPIRED to "セッション切れ (401)",
        MockScenario.FORCE_UPDATE to "強制アップデート (426)",
        MockScenario.NETWORK_ERROR to "ネットワークエラー",
        MockScenario.SERVER_ERROR to "サーバーエラー (500)",
    )

/** モックシナリオを実行中に切り替えるボトムシート。 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockScenarioSelector(onDismiss: () -> Unit) {
    var selected by remember { mutableStateOf(MockScenarioHolder.current) }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Mock Scenario",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            MockScenario.entries.forEach { scenario ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    RadioButton(
                        selected = selected == scenario,
                        onClick = {
                            selected = scenario
                            MockScenarioHolder.current = scenario
                        },
                    )
                    Text(
                        text = scenarioLabels[scenario] ?: scenario.name,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

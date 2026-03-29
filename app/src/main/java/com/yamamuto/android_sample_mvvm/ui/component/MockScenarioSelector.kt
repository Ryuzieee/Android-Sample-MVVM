package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yamamuto.android_sample_mvvm.network.MockScenario
import com.yamamuto.android_sample_mvvm.network.MockScenarioHolder

/** モックシナリオを実行中に切り替えるボトムシート。 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockScenarioSelector(onDismiss: () -> Unit) {
    var selected by remember { mutableStateOf(MockScenarioHolder.current) }
    var customCode by remember { mutableStateOf("") }
    var customMessage by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

            MockScenario.presets.forEach { (label, scenario) ->
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
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "カスタムエラー",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = customCode,
                    onValueChange = { customCode = it.filter { c -> c.isDigit() } },
                    label = { Text("ステータスコード") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = customMessage,
                    onValueChange = { customMessage = it },
                    label = { Text("メッセージ") },
                    singleLine = true,
                    modifier = Modifier.weight(1.5f),
                )
            }
            TextButton(
                onClick = {
                    val code = customCode.toIntOrNull() ?: return@TextButton
                    val msg = customMessage.ifBlank { "Error" }
                    val scenario = MockScenario.CustomError(code = code, message = msg)
                    selected = scenario
                    MockScenarioHolder.current = scenario
                },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("適用")
            }
        }
    }
}

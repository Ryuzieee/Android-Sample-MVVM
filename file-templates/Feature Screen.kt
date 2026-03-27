package ${PACKAGE_NAME}

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.UiStateContent

@Composable
fun ${NAME}Screen(
    onBack: () -> Unit,
    viewModel: ${NAME}ViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        title = { Text("${NAME}") },
        onBack = onBack,
    ) { padding ->
        UiStateContent(
            state = uiState.contentState,
            modifier = Modifier.padding(padding),
            onRetry = viewModel::retry,
        ) { data ->
            // TODO: コンテンツを実装する
        }
    }
}

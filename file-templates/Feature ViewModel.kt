package ${PACKAGE_NAME}

import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import com.yamamuto.android_sample_mvvm.ui.util.loadAsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ${NAME}ViewModel
    @Inject
    constructor(
        // TODO: UseCase を注入する
    ) : UiStateViewModel<${NAME}UiState>(${NAME}UiState()) {
        init {
            load()
        }

        fun retry() {
            load()
        }

        private fun load() {
            viewModelScope.launch {
                updateState { copy(contentState = UiState.Loading) }
                // TODO: データ取得処理を実装する
                // val result = loadAsUiState { useCase() }
                // updateState { copy(contentState = result) }
            }
        }
    }

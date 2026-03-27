package ${PACKAGE_NAME}

import com.yamamuto.android_sample_mvvm.domain.model.UiState

data class ${NAME}UiState(
    val contentState: UiState<Unit> = UiState.Loading, // TODO: Unit を適切な型に変更する
)

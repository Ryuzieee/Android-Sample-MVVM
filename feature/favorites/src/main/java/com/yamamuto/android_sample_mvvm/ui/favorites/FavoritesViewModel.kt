package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.GetFavoritesUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** お気に入り一覧画面のViewModel。 */
@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        private val getFavoritesUseCase: GetFavoritesUseCase,
    ) : UiStateViewModel<FavoritesUiState>(FavoritesUiState()) {
        init {
            viewModelScope.launch {
                getFavoritesUseCase().collect {
                    updateState { copy(contentState = UiState.Success(it)) }
                }
            }
        }
    }

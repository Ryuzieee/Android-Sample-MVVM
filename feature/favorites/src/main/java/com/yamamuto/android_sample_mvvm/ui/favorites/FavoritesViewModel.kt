package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** お気に入り一覧画面のViewModel。 */
@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        private val getFavoritesUseCase: GetFavoritesUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(FavoritesUiState())
        val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                getFavoritesUseCase().collect {
                    _uiState.value = FavoritesUiState(contentState = UiState.Success(it))
                }
            }
        }
    }

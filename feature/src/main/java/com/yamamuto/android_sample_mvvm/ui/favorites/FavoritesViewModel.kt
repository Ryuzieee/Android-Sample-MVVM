package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.usecase.GetFavoritesUseCase
import com.yamamuto.android_sample_mvvm.ui.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** お気に入り一覧画面のViewModel。 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() {
        load()
    }

    fun refresh() {
        load(forceRefresh = true)
    }

    private fun load(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = forceRefresh) }
            val content = getFavoritesUseCase().toUiState()
            _uiState.update { it.copy(content = content, isRefreshing = false) }
        }
    }
}

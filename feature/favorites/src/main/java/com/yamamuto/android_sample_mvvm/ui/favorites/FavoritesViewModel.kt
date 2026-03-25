package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.usecase.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** お気に入り一覧画面のViewModel。 */
@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        getFavoritesUseCase: GetFavoritesUseCase,
    ) : ViewModel() {
        val uiState: StateFlow<FavoritesUiState> =
            getFavoritesUseCase()
                .map { FavoritesUiState(favorites = it) }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FavoritesUiState())
    }

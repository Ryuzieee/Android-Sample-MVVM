package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.Favorite
import com.yamamuto.android_sample_mvvm.domain.usecase.GetFavoritesUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiState
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
    ) : UiStateViewModel<UiState<List<Favorite>>>(UiState.Loading) {
        init {
            viewModelScope.launch {
                getFavoritesUseCase().collect {
                    updateState { UiState.Success(it) }
                }
            }
        }
    }

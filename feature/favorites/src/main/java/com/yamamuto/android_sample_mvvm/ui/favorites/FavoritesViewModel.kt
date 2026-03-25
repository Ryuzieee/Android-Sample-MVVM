package com.yamamuto.android_sample_mvvm.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.Favorite
import com.yamamuto.android_sample_mvvm.domain.usecase.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * お気に入り一覧画面のViewModel。
 *
 * [GetFavoritesUseCase] を通じてお気に入りポケモン一覧を Flow で取得する。
 */
@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        getFavoritesUseCase: GetFavoritesUseCase,
    ) : ViewModel() {
        val favorites: StateFlow<List<Favorite>> =
            getFavoritesUseCase()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    }

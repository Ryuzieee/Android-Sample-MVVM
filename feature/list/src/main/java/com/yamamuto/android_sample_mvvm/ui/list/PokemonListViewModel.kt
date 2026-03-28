package com.yamamuto.android_sample_mvvm.ui.list

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/** ポケモン一覧画面のViewModel。 */
@HiltViewModel
class PokemonListViewModel
    @Inject
    constructor(
        private val getPokemonListUseCase: GetPokemonListUseCase,
    ) : UiStateViewModel<PokemonListUiState>(PokemonListUiState()) {
        init {
            load()
        }

        private fun load() {
            updateState { copy(pagingData = getPokemonListUseCase().cachedIn(viewModelScope)) }
        }
    }

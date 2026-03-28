package com.yamamuto.android_sample_mvvm.ui.list

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yamamuto.android_sample_mvvm.domain.usecase.ObservePokemonListUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ポケモン一覧画面のViewModel。 */
@HiltViewModel
class PokemonListViewModel
    @Inject
    constructor(
        private val observePokemonListUseCase: ObservePokemonListUseCase,
    ) : UiStateViewModel<PokemonListUiState>(PokemonListUiState()) {
        init {
            load()
        }

        private fun load() {
            viewModelScope.launch {
                observePokemonListUseCase()
                    .cachedIn(viewModelScope)
                    .collect { updateState { copy(pagingData = it) } }
            }
        }
    }

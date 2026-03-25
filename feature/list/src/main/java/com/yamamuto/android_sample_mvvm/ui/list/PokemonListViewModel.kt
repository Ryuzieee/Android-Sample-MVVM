package com.yamamuto.android_sample_mvvm.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** ポケモン一覧画面のViewModel。 */
@HiltViewModel
class PokemonListViewModel
    @Inject
    constructor(
        getPokemonListUseCase: GetPokemonListUseCase,
    ) : ViewModel() {
        val pokemonPagingFlow: Flow<PagingData<Pokemon>> =
            getPokemonListUseCase().cachedIn(viewModelScope)
    }

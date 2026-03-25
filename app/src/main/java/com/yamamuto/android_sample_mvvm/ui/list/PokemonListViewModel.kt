package com.yamamuto.android_sample_mvvm.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ポケモン一覧画面のUI状態。 */
sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState

    data class Success(val pokemons: List<Pokemon>) : PokemonListUiState

    data class Error(val message: String) : PokemonListUiState
}

/**
 * ポケモン一覧画面のViewModel。
 *
 * [GetPokemonListUseCase] を通じてポケモン一覧を取得し、[PokemonListUiState] として公開する。
 */
@HiltViewModel
class PokemonListViewModel
    @Inject
    constructor(
        private val getPokemonListUseCase: GetPokemonListUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
        val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

        init {
            loadPokemonList()
        }

        private fun loadPokemonList() {
            viewModelScope.launch {
                _uiState.value = PokemonListUiState.Loading
                _uiState.value =
                    try {
                        val pokemons = getPokemonListUseCase(limit = 20)
                        PokemonListUiState.Success(pokemons)
                    } catch (e: Exception) {
                        PokemonListUiState.Error(e.message ?: "Unknown error")
                    }
            }
        }
    }

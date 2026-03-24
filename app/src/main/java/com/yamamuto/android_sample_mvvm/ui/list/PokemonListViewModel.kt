package com.yamamuto.android_sample_mvvm.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yamamuto.android_sample_mvvm.AppContainer
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.launch

sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState
    data class Success(val pokemons: List<Pokemon>) : PokemonListUiState
    data class Error(val message: String) : PokemonListUiState
}

class PokemonListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
) : ViewModel() {

    var uiState by mutableStateOf<PokemonListUiState>(PokemonListUiState.Loading)
        private set

    init {
        loadPokemonList()
    }

    private fun loadPokemonList() {
        viewModelScope.launch {
            uiState = PokemonListUiState.Loading
            uiState = try {
                val pokemons = getPokemonListUseCase(limit = 20)
                PokemonListUiState.Success(pokemons)
            } catch (e: Exception) {
                PokemonListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        fun factory() = viewModelFactory {
            initializer {
                PokemonListViewModel(
                    GetPokemonListUseCase(AppContainer.repository)
                )
            }
        }
    }
}

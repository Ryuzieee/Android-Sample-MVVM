package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yamamuto.android_sample_mvvm.AppContainer
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.launch

sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val detail: PokemonDetail) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}

class PokemonDetailViewModel(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val pokemonName: String,
) : ViewModel() {

    var uiState by mutableStateOf<PokemonDetailUiState>(PokemonDetailUiState.Loading)
        private set

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            uiState = PokemonDetailUiState.Loading
            uiState = try {
                val detail = getPokemonDetailUseCase(pokemonName)
                PokemonDetailUiState.Success(detail)
            } catch (e: Exception) {
                PokemonDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        fun factory(pokemonName: String) = viewModelFactory {
            initializer {
                PokemonDetailViewModel(
                    GetPokemonDetailUseCase(AppContainer.repository),
                    pokemonName,
                )
            }
        }
    }
}

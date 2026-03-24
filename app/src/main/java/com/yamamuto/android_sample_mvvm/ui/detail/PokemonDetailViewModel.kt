package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ポケモン詳細画面のUI状態。 */
sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val detail: PokemonDetail) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}

/**
 * ポケモン詳細画面のViewModel。
 *
 * [GetPokemonDetailUseCase] を通じて指定ポケモンの詳細を取得し、[PokemonDetailUiState] として公開する。
 * ポケモン名は [SavedStateHandle] からナビゲーション引数として取得する。
 */
@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val pokemonName: String = checkNotNull(savedStateHandle["name"])

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
}

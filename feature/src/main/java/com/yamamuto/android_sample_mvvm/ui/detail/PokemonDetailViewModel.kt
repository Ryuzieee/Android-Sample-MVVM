package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonFullDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.ui.util.getOrNull
import com.yamamuto.android_sample_mvvm.ui.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ポケモン詳細画面のViewModel。
 *
 * [GetPokemonFullDetailUseCase] を通じて詳細・種族情報・進化チェーン・特性日本語名を
 * 一括取得し、お気に入り状態とともに [PokemonDetailUiState] として公開する。
 */
@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonFullDetailUseCase: GetPokemonFullDetailUseCase,
    private val getIsFavoriteUseCase: GetIsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    companion object {
        const val KEY_NAME = "name"
    }

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    val pokemonName: String = checkNotNull(savedStateHandle[KEY_NAME])

    init {
        load()
    }

    fun retry() {
        load()
    }

    fun refresh() {
        load(forceRefresh = true)
    }

    fun toggleFavorite() {
        val state = _uiState.value
        val fullDetail = state.content.getOrNull() ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(fullDetail.detail, state.isFavorite)
            _uiState.update { it.copy(isFavorite = !state.isFavorite) }
        }
    }

    private fun load(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(content = UiState.Loading, isRefreshing = forceRefresh) }

            val content = getPokemonFullDetailUseCase(pokemonName, forceRefresh = forceRefresh).toUiState()
            _uiState.update { it.copy(content = content, isRefreshing = false) }

            if (content is UiState.Success) {
                loadFavorite(content.data.detail.id)
            }
        }
    }

    private suspend fun loadFavorite(pokemonId: Int) {
        val isFavorite = getIsFavoriteUseCase(pokemonId).getOrElse { false }
        _uiState.update { it.copy(isFavorite = isFavorite) }
    }
}

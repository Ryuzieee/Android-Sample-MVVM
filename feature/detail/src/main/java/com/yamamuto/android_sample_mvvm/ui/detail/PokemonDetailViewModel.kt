package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.ErrorMessages
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonFullDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ObserveIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import com.yamamuto.android_sample_mvvm.ui.util.getOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle,
) : UiStateViewModel<PokemonDetailUiState>(PokemonDetailUiState()) {
    companion object {
        const val KEY_NAME = "name"
    }

    private val pokemonName: String = checkNotNull(savedStateHandle[KEY_NAME])

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
        val state = currentState
        val detail = state.contentState.getOrNull() ?: return
        viewModelScope.launch { toggleFavoriteUseCase(detail, state.isFavorite) }
    }

    private fun load(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            updateState { copy(contentState = UiState.Loading, isRefreshing = forceRefresh) }

            val result = getPokemonFullDetailUseCase(pokemonName, forceRefresh = forceRefresh)
            when {
                result.isSuccess -> {
                    val fullDetail = result.getOrThrow()
                    updateState {
                        copy(
                            contentState = UiState.Success(fullDetail.detail),
                            species = fullDetail.species,
                            evolutionChain = fullDetail.evolutionChain,
                            isRefreshing = false,
                        )
                    }
                    observeFavorite(fullDetail.detail.id)
                }

                result.isFailure -> {
                    val message = result.exceptionOrNull()?.message ?: ErrorMessages.UNKNOWN_ERROR
                    updateState {
                        copy(
                            contentState = UiState.Error(message = message),
                            isRefreshing = false,
                        )
                    }
                    sendEvent(UiEvent.ShowSnackbar(message))
                }
            }
        }
    }

    private suspend fun observeFavorite(pokemonId: Int) {
        observeIsFavoriteUseCase(pokemonId).collect { isFavorite ->
            updateState { copy(isFavorite = isFavorite) }
        }
    }
}

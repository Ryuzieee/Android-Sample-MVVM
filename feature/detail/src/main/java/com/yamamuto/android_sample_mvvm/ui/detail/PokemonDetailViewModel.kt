package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.ui.base.BaseViewModel
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ポケモン詳細画面のViewModel。
 *
 * [GetPokemonDetailUseCase] を通じて指定ポケモンの詳細を取得し、お気に入り状態とともに
 * [PokemonDetailUiState] として公開する。
 */
@HiltViewModel
class PokemonDetailViewModel
    @Inject
    constructor(
        private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
        private val getIsFavoriteUseCase: GetIsFavoriteUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
        savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<PokemonDetailUiState>(PokemonDetailUiState()) {
        private val pokemonName: String = checkNotNull(savedStateHandle["name"])

        init {
            load()
        }

        fun retry() {
            load()
        }

        fun toggleFavorite() {
            val state = currentState
            val detail = (state.contentState as? UiState.Success)?.data ?: return
            viewModelScope.launch { toggleFavoriteUseCase(detail, state.isFavorite) }
        }

        private fun load() {
            viewModelScope.launch {
                    updateState { it.copy(contentState = UiState.Loading) }
                    runCatching { getPokemonDetailUseCase(pokemonName) }.fold(
                        onSuccess = { observeFavorite(it) },
                        onFailure = { handleError(it) },
                    )
                }
        }

        private suspend fun observeFavorite(detail: PokemonDetail) {
            getIsFavoriteUseCase(detail.id).collect { isFavorite ->
                updateState(PokemonDetailUiState(UiState.Success(detail), isFavorite))
            }
        }

        private suspend fun handleError(e: Throwable) {
            Timber.e(e, "Failed to load detail: $pokemonName")
            sendEvent(UiEvent.ShowSnackbar(e.message ?: "エラーが発生しました"))
            updateState {
                it.copy(
                    contentState = UiState.Error(
                        message = e.message ?: "不明なエラーが発生しました",
                        isNetworkError = e is AppException.Network,
                    ),
                )
            }
        }
    }

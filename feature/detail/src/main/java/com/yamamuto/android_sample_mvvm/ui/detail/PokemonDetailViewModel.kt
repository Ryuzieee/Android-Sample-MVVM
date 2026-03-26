package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import com.yamamuto.android_sample_mvvm.ui.util.loadAsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    ) : ViewModel() {
        private val pokemonName: String = checkNotNull(savedStateHandle["name"])

        private val _uiState = MutableStateFlow(PokemonDetailUiState())
        val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

        private val _events = Channel<UiEvent>(Channel.BUFFERED)
        val events: Flow<UiEvent> = _events.receiveAsFlow()

        init {
            load()
        }

        fun retry() {
            load()
        }

        fun toggleFavorite() {
            val state = _uiState.value
            val detail = (state.contentState as? UiState.Success)?.data ?: return
            viewModelScope.launch { toggleFavoriteUseCase(detail, state.isFavorite) }
        }

        private fun load() {
            viewModelScope.launch {
                _uiState.update { it.copy(contentState = UiState.Loading) }
                val result = loadAsUiState { getPokemonDetailUseCase(pokemonName) }
                when (result) {
                    is UiState.Success -> observeFavorite(result.data)
                    is UiState.Error -> {
                        _events.send(UiEvent.ShowSnackbar(result.message))
                        _uiState.update { it.copy(contentState = result) }
                    }
                    is UiState.Loading, is UiState.Idle -> {}
                }
            }
        }

        private suspend fun observeFavorite(detail: PokemonDetail) {
            getIsFavoriteUseCase(detail.id).collect { isFavorite ->
                _uiState.value = PokemonDetailUiState(UiState.Success(detail), isFavorite)
            }
        }
    }

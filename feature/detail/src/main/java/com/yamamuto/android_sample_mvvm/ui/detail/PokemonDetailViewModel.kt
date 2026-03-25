@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ポケモン詳細画面のViewModel。
 *
 * [GetPokemonDetailUseCase] を通じて指定ポケモンの詳細を取得し、[UiState] として公開する。
 * お気に入り状態は [isFavorite] で監視し、[toggleFavorite] でトグルできる。
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

        private val _uiState = MutableStateFlow<UiState<PokemonDetail>>(UiState.Loading)
        val uiState: StateFlow<UiState<PokemonDetail>> = _uiState.asStateFlow()

        val isFavorite: StateFlow<Boolean> =
            _uiState
                .flatMapLatest { state ->
                    when (state) {
                        is UiState.Success -> getIsFavoriteUseCase(state.data.id)
                        else -> flowOf(false)
                    }
                }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

        private val _events = Channel<UiEvent>(Channel.BUFFERED)
        val events = _events.receiveAsFlow()

        init {
            loadDetail()
        }

        fun retry() {
            loadDetail()
        }

        fun toggleFavorite() {
            val detail = (uiState.value as? UiState.Success)?.data ?: return
            viewModelScope.launch {
                toggleFavoriteUseCase(detail, isFavorite.value)
            }
        }

        private fun loadDetail() {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                _uiState.value =
                    runCatching { getPokemonDetailUseCase(pokemonName) }.fold(
                        onSuccess = { UiState.Success(it) },
                        onFailure = { e ->
                            Timber.e(e, "Failed to load detail: $pokemonName")
                            _events.send(UiEvent.ShowSnackbar(e.message ?: "エラーが発生しました"))
                            UiState.Error(
                                message = e.message ?: "不明なエラーが発生しました",
                                isNetworkError = e is AppException.Network,
                            )
                        },
                    )
            }
        }
    }

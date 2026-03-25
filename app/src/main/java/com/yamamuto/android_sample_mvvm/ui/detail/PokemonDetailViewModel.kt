package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ポケモン詳細画面のViewModel。
 *
 * [GetPokemonDetailUseCase] を通じて指定ポケモンの詳細を取得し、[UiState] として公開する。
 * ポケモン名は [SavedStateHandle] からナビゲーション引数として取得する。
 */
@HiltViewModel
class PokemonDetailViewModel
    @Inject
    constructor(
        private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val pokemonName: String = checkNotNull(savedStateHandle["name"])

        private val _uiState = MutableStateFlow<UiState<PokemonDetail>>(UiState.Loading)
        val uiState: StateFlow<UiState<PokemonDetail>> = _uiState.asStateFlow()

        init {
            loadDetail()
        }

        fun retry() {
            loadDetail()
        }

        private fun loadDetail() {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                _uiState.value =
                    runCatching { getPokemonDetailUseCase(pokemonName) }.fold(
                        onSuccess = { UiState.Success(it) },
                        onFailure = { e ->
                            Timber.e(e, "Failed to load detail: $pokemonName")
                            UiState.Error(
                                message = e.message ?: "不明なエラーが発生しました",
                                isNetworkError = e is AppException.Network,
                            )
                        },
                    )
            }
        }
    }

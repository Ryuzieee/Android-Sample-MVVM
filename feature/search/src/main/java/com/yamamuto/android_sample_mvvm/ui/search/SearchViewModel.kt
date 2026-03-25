@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)

package com.yamamuto.android_sample_mvvm.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * 検索画面のViewModel。
 *
 * クエリ入力に 500ms のデバウンスを適用し、[GetPokemonDetailUseCase] でポケモンを検索する。
 * [searchResult] が null の場合は入力待ちのアイドル状態を表す。
 */
@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    ) : ViewModel() {
        private val _query = MutableStateFlow("")
        val query: StateFlow<String> = _query.asStateFlow()

        val searchResult: StateFlow<UiState<PokemonDetail>?> =
            _query
                .debounce(500)
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf<UiState<PokemonDetail>?>(null)
                    } else {
                        flow {
                            emit(UiState.Loading)
                            emit(
                                runCatching { getPokemonDetailUseCase(query.lowercase().trim()) }
                                    .fold(
                                        onSuccess = { UiState.Success(it) },
                                        onFailure = { e ->
                                            UiState.Error(
                                                message = e.message ?: "エラーが発生しました",
                                                isNetworkError = e is AppException.Network,
                                            )
                                        },
                                    ),
                            )
                        }
                    }
                }
                .stateIn(viewModelScope, SharingStarted.Lazily, null)

        fun onQueryChange(newQuery: String) {
            _query.value = newQuery
        }
    }

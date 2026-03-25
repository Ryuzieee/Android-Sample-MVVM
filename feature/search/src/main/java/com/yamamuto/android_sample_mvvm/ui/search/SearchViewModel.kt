@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)

package com.yamamuto.android_sample_mvvm.ui.search

import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.SearchPokemonUseCase
import com.yamamuto.android_sample_mvvm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 検索画面のViewModel。
 *
 * クエリ入力に 500ms のデバウンスを適用し、[SearchPokemonUseCase] でポケモン名をあいまい検索する。
 */
@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val searchPokemonUseCase: SearchPokemonUseCase,
    ) : BaseViewModel<SearchUiState>(SearchUiState()) {
        init {
            load()
        }

        fun onQueryChange(newQuery: String) {
            updateState { it.copy(query = newQuery) }
        }

        /** エラー後の再検索。[distinctUntilChanged] をバイパスして直接実行する。 */
        fun retrySearch() {
            val query = currentState.query
            if (query.isBlank()) return
            viewModelScope.launch {
                updateState { it.copy(result = UiState.Loading) }
                val result = fetchResults(query)
                updateState { it.copy(result = result) }
            }
        }

        private fun load() {
            viewModelScope.launch {
                uiState
                    .map { it.query }
                    .distinctUntilChanged()
                    .debounce(500)
                    .flatMapLatest(::search)
                    .collect { result -> updateState { it.copy(result = result) } }
            }
        }

        private fun search(query: String): Flow<UiState<List<String>>?> {
            if (query.isBlank()) return flowOf(null)
            return flow {
                emit(UiState.Loading)
                emit(fetchResults(query))
            }
        }

        private suspend fun fetchResults(query: String): UiState<List<String>> =
            runCatching { searchPokemonUseCase(query) }
                .fold(
                    onSuccess = { names ->
                        if (names.isEmpty()) {
                            UiState.Error(message = "「$query」に一致するポケモンは見つかりませんでした")
                        } else {
                            UiState.Success(names)
                        }
                    },
                    onFailure = { e ->
                        UiState.Error(
                            message = e.message ?: "エラーが発生しました",
                            isNetworkError = e is AppException.Network,
                        )
                    },
                )
    }

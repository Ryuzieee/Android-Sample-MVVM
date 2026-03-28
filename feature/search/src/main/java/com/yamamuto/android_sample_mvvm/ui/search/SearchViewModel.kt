@file:OptIn(kotlinx.coroutines.FlowPreview::class)

package com.yamamuto.android_sample_mvvm.ui.search

import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.usecase.SearchPokemonUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import com.yamamuto.android_sample_mvvm.ui.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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
    ) : UiStateViewModel<SearchUiState>(SearchUiState()) {
        init {
            observeQuery()
        }

        private fun observeQuery() {
            viewModelScope.launch {
                uiState
                    .map { it.query }
                    .distinctUntilChanged()
                    .debounce(500)
                    .collect { query -> search(query) }
            }
        }

        fun onQueryChange(newQuery: String) {
            updateState { copy(query = newQuery) }
        }

        /** エラー後の再検索。デバウンスをバイパスして直接実行する。 */
        fun retrySearch() {
            val query = currentState.query
            if (query.isBlank()) return
            viewModelScope.launch { search(query) }
        }

        private suspend fun search(query: String) {
            if (query.isBlank()) {
                updateState { copy(result = UiState.Idle) }
                return
            }
            updateState { copy(result = UiState.Loading) }
            val result = searchPokemonUseCase(query).toUiState()
            val finalResult = if (result is UiState.Success && result.data.isEmpty()) {
                UiState.Error(message = SearchStrings.noResultsMessage(query))
            } else {
                result
            }
            updateState { copy(result = finalResult) }
        }
    }

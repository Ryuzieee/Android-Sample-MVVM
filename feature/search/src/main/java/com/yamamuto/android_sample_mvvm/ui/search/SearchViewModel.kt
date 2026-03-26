@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)

package com.yamamuto.android_sample_mvvm.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.domain.usecase.SearchPokemonUseCase
import com.yamamuto.android_sample_mvvm.ui.util.loadAsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SearchUiState())
        val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                _uiState
                    .map { it.query }
                    .distinctUntilChanged()
                    .debounce(500)
                    .flatMapLatest(::search)
                    .collect { result -> _uiState.update { it.copy(result = result) } }
            }
        }

        fun onQueryChange(newQuery: String) {
            _uiState.update { it.copy(query = newQuery) }
        }

        /** エラー後の再検索。[distinctUntilChanged] をバイパスして直接実行する。 */
        fun retrySearch() {
            val query = _uiState.value.query
            if (query.isBlank()) return
            viewModelScope.launch {
                _uiState.update { it.copy(result = UiState.Loading) }
                _uiState.update { it.copy(result = fetchResults(query)) }
            }
        }

        private fun search(query: String): Flow<UiState<List<String>>> {
            if (query.isBlank()) return flowOf(UiState.Idle)
            return flow {
                emit(UiState.Loading)
                emit(fetchResults(query))
            }
        }

        private suspend fun fetchResults(query: String): UiState<List<String>> {
            val result = loadAsUiState { searchPokemonUseCase(query) }
            return if (result is UiState.Success && result.data.isEmpty()) {
                UiState.Error(message = "「$query」に一致するポケモンは見つかりませんでした")
            } else {
                result
            }
        }
    }

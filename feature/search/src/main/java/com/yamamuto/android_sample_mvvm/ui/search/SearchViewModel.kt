@file:OptIn(kotlinx.coroutines.FlowPreview::class)

package com.yamamuto.android_sample_mvvm.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.usecase.SearchPokemonUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.ui.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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
class SearchViewModel @Inject constructor(
    private val searchPokemonUseCase: SearchPokemonUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

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
        _uiState.update { it.copy(query = newQuery) }
    }

    /** エラー後の再検索。デバウンスをバイパスして直接実行する。 */
    fun retrySearch() {
        val query = _uiState.value.query
        if (query.isBlank()) return
        viewModelScope.launch { search(query) }
    }

    private suspend fun search(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(result = UiState.Idle) }
            return
        }
        _uiState.update { it.copy(result = UiState.Loading) }
        val result = searchPokemonUseCase(query).toUiState()
        _uiState.update { it.copy(result = result) }
    }
}

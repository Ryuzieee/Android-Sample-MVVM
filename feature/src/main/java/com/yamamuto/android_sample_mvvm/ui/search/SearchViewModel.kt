package com.yamamuto.android_sample_mvvm.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.usecase.SearchPokemonUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.ui.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 検索画面のViewModel。クエリ入力に 500ms のデバウンスを適用する。 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPokemonUseCase: SearchPokemonUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        searchJob?.cancel()
        searchJob =
            viewModelScope.launch {
                delay(DEBOUNCE_MS)
                search(newQuery)
            }
    }

    fun retrySearch() {
        val query = _uiState.value.query
        if (query.isBlank()) return
        viewModelScope.launch { search(query) }
    }

    private suspend fun search(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(content = UiState.Idle) }
            return
        }
        _uiState.update { it.copy(content = UiState.Loading) }
        val content = searchPokemonUseCase(query).toUiState()
        _uiState.update { it.copy(content = content) }
    }

    companion object {
        private const val DEBOUNCE_MS = 500L
    }
}

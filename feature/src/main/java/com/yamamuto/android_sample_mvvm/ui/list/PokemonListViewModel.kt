package com.yamamuto.android_sample_mvvm.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonListUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.ui.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGE_SIZE = 20

/** ポケモン一覧画面のViewModel。 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        loadFirstPage(isRefresh = false)
    }

    fun refresh() {
        loadFirstPage(isRefresh = true)
    }

    fun loadMore() {
        val state = _uiState.value
        val current = state.loadState as? UiState.Success ?: return
        if (state.isLoadingMore || !state.hasMore) return

        _uiState.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            getPokemonList(offset = current.data.size, limit = PAGE_SIZE).fold(
                onSuccess = { items ->
                    _uiState.update {
                        it.copy(
                            loadState = UiState.Success(current.data + items),
                            isLoadingMore = false,
                            hasMore = items.size >= PAGE_SIZE,
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingMore = false) }
                },
            )
        }
    }

    private fun loadFirstPage(isRefresh: Boolean) {
        _uiState.update {
            if (isRefresh) it.copy(isRefreshing = true) else it.copy(loadState = UiState.Loading)
        }
        viewModelScope.launch {
            val result = getPokemonList(offset = 0, limit = PAGE_SIZE)
            _uiState.update { current ->
                result.fold(
                    onSuccess = { items ->
                        current.copy(
                            loadState = UiState.Success(items),
                            isRefreshing = false,
                            hasMore = items.size >= PAGE_SIZE,
                        )
                    },
                    onFailure = {
                        current.copy(
                            // refresh 失敗時は既存の Success を維持し、初回失敗時のみ Error にする
                            loadState =
                                if (isRefresh && current.loadState is UiState.Success) {
                                    current.loadState
                                } else {
                                    result.toUiState()
                                },
                            isRefreshing = false,
                        )
                    },
                )
            }
        }
    }
}

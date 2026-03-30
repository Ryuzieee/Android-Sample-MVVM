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
        loadInitial()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            val result = getPokemonList(offset = 0, limit = PAGE_SIZE)
            result.fold(
                onSuccess = { items ->
                    _uiState.update {
                        it.copy(
                            items = items,
                            loadState = UiState.Success(Unit),
                            isRefreshing = false,
                            hasMore = items.size >= PAGE_SIZE,
                        )
                    }
                },
                onFailure = {
                    _uiState.update { state -> state.copy(isRefreshing = false) }
                },
            )
        }
    }

    fun loadMore() {
        val current = _uiState.value
        if (current.isLoadingMore || !current.hasMore) return

        _uiState.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            val result = getPokemonList(offset = current.items.size, limit = PAGE_SIZE)
            result.fold(
                onSuccess = { items ->
                    _uiState.update {
                        it.copy(
                            items = it.items + items,
                            isLoadingMore = false,
                            hasMore = items.size >= PAGE_SIZE,
                        )
                    }
                },
                onFailure = {
                    _uiState.update { state -> state.copy(isLoadingMore = false) }
                },
            )
        }
    }

    private fun loadInitial() {
        _uiState.update { it.copy(loadState = UiState.Loading) }
        viewModelScope.launch {
            val result = getPokemonList(offset = 0, limit = PAGE_SIZE)
            val loadState = result.map { }.toUiState()
            _uiState.update {
                it.copy(
                    items = result.getOrDefault(emptyList()),
                    loadState = loadState,
                    hasMore = (result.getOrNull()?.size ?: 0) >= PAGE_SIZE,
                )
            }
        }
    }
}

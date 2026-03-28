package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.usecase.GetEvolutionChainUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonSpeciesUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import com.yamamuto.android_sample_mvvm.ui.util.getOrNull
import com.yamamuto.android_sample_mvvm.ui.util.loadAsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ポケモン詳細画面のViewModel。
 *
 * [GetPokemonDetailUseCase] を通じて指定ポケモンの詳細を取得し、お気に入り状態とともに
 * [PokemonDetailUiState] として公開する。
 */
@HiltViewModel
class PokemonDetailViewModel
    @Inject
    constructor(
        private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
        private val getIsFavoriteUseCase: GetIsFavoriteUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
        private val getPokemonSpeciesUseCase: GetPokemonSpeciesUseCase,
        private val getEvolutionChainUseCase: GetEvolutionChainUseCase,
        savedStateHandle: SavedStateHandle,
    ) : UiStateViewModel<PokemonDetailUiState>(PokemonDetailUiState()) {
        private val pokemonName: String = checkNotNull(savedStateHandle["name"])

        init {
            load()
        }

        fun retry() {
            load()
        }

        fun toggleFavorite() {
            val state = currentState
            val detail = state.contentState.getOrNull() ?: return
            viewModelScope.launch { toggleFavoriteUseCase(detail, state.isFavorite) }
        }

        private fun load() {
            viewModelScope.launch {
                updateState { copy(contentState = UiState.Loading) }
                val result = loadAsUiState { getPokemonDetailUseCase(pokemonName) }
                when (result) {
                    is UiState.Success -> {
                        loadSpeciesAndEvolution()
                        observeFavorite(result.data) // collect で永続サスペンドするので最後に呼ぶ
                    }
                    is UiState.Error -> {
                        sendEvent(UiEvent.ShowSnackbar(result.message))
                        updateState { copy(contentState = result) }
                    }
                    is UiState.Loading, is UiState.Idle -> {}
                }
            }
        }

        private fun loadSpeciesAndEvolution() {
            viewModelScope.launch {
                runCatching { getPokemonSpeciesUseCase(pokemonName) }
                    .onSuccess { species -> updateState { copy(species = species) } }
            }
            viewModelScope.launch {
                runCatching { getEvolutionChainUseCase(pokemonName) }
                    .onSuccess { chain -> updateState { copy(evolutionChain = chain) } }
            }
        }

        private suspend fun observeFavorite(detail: PokemonDetail) {
            getIsFavoriteUseCase(detail.id).collect { isFavorite ->
                updateState { copy(contentState = UiState.Success(detail), isFavorite = isFavorite) }
            }
        }
    }

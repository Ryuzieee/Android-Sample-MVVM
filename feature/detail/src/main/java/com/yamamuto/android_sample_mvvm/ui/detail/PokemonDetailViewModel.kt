package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import com.yamamuto.android_sample_mvvm.domain.usecase.GetEvolutionChainUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetIsFavoriteUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonDetailUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetPokemonSpeciesUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ToggleFavoriteUseCase
import com.yamamuto.android_sample_mvvm.ui.util.UiEvent
import com.yamamuto.android_sample_mvvm.ui.util.UiState
import com.yamamuto.android_sample_mvvm.ui.util.UiStateViewModel
import com.yamamuto.android_sample_mvvm.ui.util.getOrNull
import com.yamamuto.android_sample_mvvm.ui.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
        private val repository: PokemonRepository,
        savedStateHandle: SavedStateHandle,
    ) : UiStateViewModel<PokemonDetailUiState>(PokemonDetailUiState()) {
        private val pokemonName: String = checkNotNull(savedStateHandle["name"])

        init {
            load()
        }

        fun retry() {
            load()
        }

        fun refresh() {
            viewModelScope.launch {
                updateState { copy(isRefreshing = true) }
                val result = getPokemonDetailUseCase(pokemonName, forceRefresh = true).toUiState()
                if (result is UiState.Success) {
                    loadSpeciesAndEvolution(result.data)
                }
                updateState { copy(contentState = result, isRefreshing = false) }
            }
        }

        fun toggleFavorite() {
            val state = currentState
            val detail = state.contentState.getOrNull() ?: return
            viewModelScope.launch { toggleFavoriteUseCase(detail, state.isFavorite) }
        }

        private fun load() {
            viewModelScope.launch {
                updateState { copy(contentState = UiState.Loading) }
                val result = getPokemonDetailUseCase(pokemonName)
                val uiResult = result.toUiState()
                when (uiResult) {
                    is UiState.Success -> {
                        loadSpeciesAndEvolution(uiResult.data)
                        observeFavorite(uiResult.data) // collect で永続サスペンドするので最後に呼ぶ
                    }
                    is UiState.Error -> {
                        sendEvent(UiEvent.ShowSnackbar(uiResult.message))
                        updateState { copy(contentState = uiResult) }
                    }
                    is UiState.Loading, is UiState.Idle -> {}
                }
            }
        }

        private fun loadSpeciesAndEvolution(detail: PokemonDetail) {
            viewModelScope.launch {
                getPokemonSpeciesUseCase(pokemonName)
                    .onSuccess { species -> updateState { copy(species = species) } }
            }
            viewModelScope.launch {
                getEvolutionChainUseCase(pokemonName)
                    .onSuccess { chain -> updateState { copy(evolutionChain = chain) } }
            }
            // 特性の日本語名を並列取得
            viewModelScope.launch {
                val jaNames = detail.abilities.map { ability ->
                    async {
                        repository.getAbilityJapaneseName(ability.name).getOrDefault(ability.name)
                    }
                }.awaitAll()
                val updatedAbilities = detail.abilities.zip(jaNames) { ability, jaName ->
                    ability.copy(japaneseName = jaName)
                }
                val updatedDetail = detail.copy(abilities = updatedAbilities)
                updateState { copy(contentState = UiState.Success(updatedDetail)) }
            }
        }

        private suspend fun observeFavorite(detail: PokemonDetail) {
            getIsFavoriteUseCase(detail.id).collect { isFavorite ->
                updateState { copy(isFavorite = isFavorite) }
            }
        }
    }

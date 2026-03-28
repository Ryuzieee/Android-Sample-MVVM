package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.usecase.GetAbilityJapaneseNameUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.GetEvolutionChainUseCase
import com.yamamuto.android_sample_mvvm.domain.usecase.ObserveIsFavoriteUseCase
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
        private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
        private val getPokemonSpeciesUseCase: GetPokemonSpeciesUseCase,
        private val getEvolutionChainUseCase: GetEvolutionChainUseCase,
        private val getAbilityJapaneseNameUseCase: GetAbilityJapaneseNameUseCase,
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
            load(forceRefresh = true)
        }

        fun toggleFavorite() {
            val state = currentState
            val detail = state.contentState.getOrNull() ?: return
            viewModelScope.launch { toggleFavoriteUseCase(detail, state.isFavorite) }
        }

        private fun load(forceRefresh: Boolean = false) {
            viewModelScope.launch {
                updateState { copy(contentState = UiState.Loading, isRefreshing = forceRefresh) }

                val result = getPokemonDetailUseCase(pokemonName, forceRefresh = forceRefresh).toUiState()
                updateState { copy(contentState = result, isRefreshing = false) }

                if (result is UiState.Success) {
                    loadSubData(result.data)
                    observeFavorite(result.data) // collect で永続サスペンドするので最後に呼ぶ
                }
                if (result is UiState.Error) {
                    sendEvent(UiEvent.ShowSnackbar(result.message))
                }
            }
        }

        private fun loadSubData(detail: PokemonDetail) {
            viewModelScope.launch {
                val species = getPokemonSpeciesUseCase(pokemonName).getOrNull()
                if (species != null) updateState { copy(species = species) }
            }
            viewModelScope.launch {
                val chain = getEvolutionChainUseCase(pokemonName).getOrNull()
                if (chain != null) updateState { copy(evolutionChain = chain) }
            }
            viewModelScope.launch {
                val jaNames = detail.abilities.map { ability ->
                    async { getAbilityJapaneseNameUseCase(ability.name).getOrDefault(ability.name) }
                }.awaitAll()
                val updatedAbilities = detail.abilities.zip(jaNames) { ability, jaName ->
                    ability.copy(japaneseName = jaName)
                }
                updateState { copy(contentState = UiState.Success(detail.copy(abilities = updatedAbilities))) }
            }
        }

        private suspend fun observeFavorite(detail: PokemonDetail) {
            observeIsFavoriteUseCase(detail.id).collect { isFavorite ->
                updateState { copy(isFavorite = isFavorite) }
            }
        }
    }

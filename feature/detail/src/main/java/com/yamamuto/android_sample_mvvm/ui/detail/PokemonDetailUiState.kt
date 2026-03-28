package com.yamamuto.android_sample_mvvm.ui.detail

import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/** ポケモン詳細画面のUI状態。 */
data class PokemonDetailUiState(
    val contentState: UiState<PokemonDetailModel> = UiState.Loading,
    val isFavorite: Boolean = false,
    val isRefreshing: Boolean = false,
    val species: PokemonSpeciesModel? = null,
    val evolutionChain: List<EvolutionStageModel> = emptyList(),
)

package com.yamamuto.android_sample_mvvm.domain.model

/** 詳細画面に必要な全データを集約したモデル。 */
data class PokemonFullDetailModel(
    val detail: PokemonDetailModel,
    val species: PokemonSpeciesModel?,
    val evolutionChain: List<EvolutionStageModel>,
)

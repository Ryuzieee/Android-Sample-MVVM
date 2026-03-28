package com.yamamuto.android_sample_mvvm.domain.model

/**
 * 詳細画面に必要な全データを集約したモデル。
 *
 * [GetPokemonFullDetailUseCase] が並列取得した結果を1つにまとめる。
 */
data class PokemonFullDetailModel(
    val detail: PokemonDetailModel,
    val species: PokemonSpeciesModel?,
    val evolutionChain: List<EvolutionStageModel>,
)

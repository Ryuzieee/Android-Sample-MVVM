package com.yamamuto.android_sample_mvvm.domain.model

/**
 * 詳細画面に必要な全データを集約したモデル。
 *
 * [GetPokemonFullDetailUseCase] が並列取得した結果を1つにまとめる。
 */
data class PokemonFullDetail(
    val detail: PokemonDetail,
    val species: PokemonSpecies?,
    val evolutionChain: List<EvolutionStage>,
)

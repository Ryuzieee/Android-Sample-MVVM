package com.yamamuto.android_sample_mvvm.domain.model

/** 進化チェーンの1段階を表す。 */
data class EvolutionStage(
    val name: String,
    val japaneseName: String,
    val id: Int,
    val imageUrl: String,
    val minLevel: Int?,
)

package com.yamamuto.android_sample_mvvm.data.api.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** evolution-chain API レスポンスの DTO。 */
@InternalSerializationApi
@Serializable
data class EvolutionChainResponse(
    val id: Int,
    val chain: ChainLink,
) {
    @Serializable
    data class ChainLink(
        val species: Species,
        @SerialName("evolves_to") val evolvesTo: List<ChainLink>,
        @SerialName("evolution_details") val evolutionDetails: List<EvolutionDetail> = emptyList(),
    )

    @Serializable
    data class Species(
        val name: String,
        val url: String,
    )

    @Serializable
    data class EvolutionDetail(
        @SerialName("min_level") val minLevel: Int? = null,
        val trigger: Trigger? = null,
    )

    @Serializable
    data class Trigger(
        val name: String,
    )
}

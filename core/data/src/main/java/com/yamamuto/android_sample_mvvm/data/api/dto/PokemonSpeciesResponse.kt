package com.yamamuto.android_sample_mvvm.data.api.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** pokemon-species API レスポンスの DTO。 */
@InternalSerializationApi
@Serializable
data class PokemonSpeciesResponse(
    @SerialName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>,
    @SerialName("evolution_chain") val evolutionChain: EvolutionChainRef,
    val genera: List<Genus>,
    @SerialName("egg_groups") val eggGroups: List<NamedResource>,
    @SerialName("gender_rate") val genderRate: Int,
    @SerialName("capture_rate") val captureRate: Int,
    val habitat: NamedResource?,
    val generation: NamedResource,
) {
    @Serializable
    data class FlavorTextEntry(
        @SerialName("flavor_text") val flavorText: String,
        val language: NamedResource,
        val version: NamedResource,
    )

    @Serializable
    data class Genus(
        val genus: String,
        val language: NamedResource,
    )

    @Serializable
    data class EvolutionChainRef(
        val url: String,
    )

    @Serializable
    data class NamedResource(
        val name: String,
        val url: String = "",
    )
}

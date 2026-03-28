@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import kotlinx.serialization.InternalSerializationApi

private const val ARTWORK_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

/** Species DTO → Domain */
internal fun PokemonSpeciesResponse.toDomain(): PokemonSpeciesModel {
    val jaName = names.firstOrNull { it.language.name == "ja" }?.name
        ?: names.firstOrNull { it.language.name == "ja-hrkt" }?.name
        ?: ""

    val jaFlavorText = flavorTextEntries
        .lastOrNull { it.language.name == "ja" }
        ?.flavorText
        ?.replace("\n", " ")
        ?.replace("\u000c", " ")
        ?: flavorTextEntries.lastOrNull { it.language.name == "en" }?.flavorText.orEmpty()

    val jaGenus = genera
        .firstOrNull { it.language.name == "ja" }
        ?.genus
        ?: genera.firstOrNull { it.language.name == "en" }?.genus.orEmpty()

    return PokemonSpeciesModel(
        japaneseName = jaName,
        flavorText = jaFlavorText,
        genus = jaGenus,
        eggGroups = eggGroups.map { it.name },
        genderRate = genderRate,
        captureRate = captureRate,
        habitat = habitat?.name,
        generation = generation.name,
        evolutionChainUrl = evolutionChain.url,
    )
}

/** EvolutionChain DTO → Domain (フラットなリストに展開) */
internal fun EvolutionChainResponse.toStages(): List<EvolutionStageModel> {
    val stages = mutableListOf<EvolutionStageModel>()
    fun walk(link: EvolutionChainResponse.ChainLink) {
        val id = extractIdFromUrl(link.species.url)
        val minLevel = link.evolutionDetails.firstOrNull()?.minLevel
        stages += EvolutionStageModel(
            name = link.species.name,
            japaneseName = "",
            id = id,
            imageUrl = "${ARTWORK_URL}$id.png",
            minLevel = minLevel,
        )
        link.evolvesTo.forEach { walk(it) }
    }
    walk(chain)
    return stages
}

/** species URL (e.g. "https://pokeapi.co/api/v2/pokemon-species/2/") から ID を抽出 */
internal fun extractIdFromUrl(url: String): Int {
    return url.trimEnd('/').substringAfterLast('/').toInt()
}

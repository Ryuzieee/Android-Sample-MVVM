@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.util.LanguageCodes
import kotlinx.serialization.InternalSerializationApi

private const val ARTWORK_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

/** Species DTO → Domain */
internal fun PokemonSpeciesResponse.toDomain(): PokemonSpeciesModel {
    val jaName = names.firstOrNull { it.language.name == LanguageCodes.JA }?.name
        ?: names.firstOrNull { it.language.name == LanguageCodes.JA_HRKT }?.name
        ?: ""

    val jaFlavorText = flavorTextEntries
        .lastOrNull { it.language.name == LanguageCodes.JA }
        ?.flavorText
        ?.replace("\n", " ")
        ?.replace("\u000c", " ")
        ?: flavorTextEntries.lastOrNull { it.language.name == LanguageCodes.EN }?.flavorText.orEmpty()

    val jaGenus = genera
        .firstOrNull { it.language.name == LanguageCodes.JA }
        ?.genus
        ?: genera.firstOrNull { it.language.name == LanguageCodes.EN }?.genus.orEmpty()

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

@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.api.response.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.response.PokemonSpeciesResponse
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.util.LanguageCodes
import com.yamamuto.android_sample_mvvm.domain.util.LanguageCodes.japaneseName
import kotlinx.serialization.InternalSerializationApi

private const val ARTWORK_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

internal fun PokemonSpeciesResponse.toModel(): PokemonSpeciesModel {
    val jaName = names.associate { it.language.name to it.name }.japaneseName()

    val jaFlavorText =
        flavorTextEntries
            .lastOrNull { it.language.name == LanguageCodes.JA }
            ?.flavorText
            ?.replace("\n", " ")
            ?.replace("\u000c", " ")
            ?: flavorTextEntries.lastOrNull { it.language.name == LanguageCodes.EN }?.flavorText.orEmpty()

    val jaGenus =
        genera
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

/** 進化チェーンをフラットなリストに展開する。 */
internal fun EvolutionChainResponse.toModel(): List<EvolutionStageModel> {
    val stages = mutableListOf<EvolutionStageModel>()

    fun walk(link: EvolutionChainResponse.ChainLink) {
        val id = extractIdFromUrl(link.species.url)
        val minLevel = link.evolutionDetails.firstOrNull()?.minLevel
        stages +=
            EvolutionStageModel(
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

internal fun extractIdFromUrl(url: String): Int {
    return url.trimEnd('/').substringAfterLast('/').toInt()
}

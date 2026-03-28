@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import kotlinx.serialization.InternalSerializationApi

private const val TYPE_DELIMITER = ","
private const val ENTRY_DELIMITER = ";"
private const val FIELD_DELIMITER = ":"

/** DTO → Domain */
internal fun PokemonDetailResponse.toDomain(): PokemonDetailModel {
    return PokemonDetailModel(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        types = types.map { it.type.name },
        abilities = abilities.map {
            PokemonDetailModel.Ability(name = it.ability.name, isHidden = it.isHidden)
        },
        imageUrl = sprites.other.officialArtwork.frontDefault,
        stats = stats.map { PokemonDetailModel.Stat(name = it.stat.name, value = it.baseStat) },
    )
}

/** Entity → Domain */
internal fun PokemonDetailEntity.toDomain(): PokemonDetailModel {
    return PokemonDetailModel(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        types = types.split(TYPE_DELIMITER),
        abilities = abilities.split(ENTRY_DELIMITER).mapNotNull { entry ->
            val parts = entry.split(FIELD_DELIMITER)
            when (parts.size) {
                3 -> PokemonDetailModel.Ability(parts[0], parts[1], parts[2].toBooleanStrictOrNull() ?: false)
                2 -> PokemonDetailModel.Ability(parts[0], "", parts[1].toBooleanStrictOrNull() ?: false)
                else -> null
            }
        },
        imageUrl = imageUrl,
        stats = stats.split(ENTRY_DELIMITER).mapNotNull { entry ->
            val parts = entry.split(FIELD_DELIMITER)
            if (parts.size == 2) PokemonDetailModel.Stat(parts[0], parts[1].toInt()) else null
        },
    )
}

/** Domain → Entity */
internal fun PokemonDetailModel.toEntity(): PokemonDetailEntity {
    return PokemonDetailEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        types = types.joinToString(TYPE_DELIMITER),
        abilities = abilities.joinToString(ENTRY_DELIMITER) { "${it.name}${FIELD_DELIMITER}${it.japaneseName}${FIELD_DELIMITER}${it.isHidden}" },
        imageUrl = imageUrl,
        stats = stats.joinToString(ENTRY_DELIMITER) { "${it.name}${FIELD_DELIMITER}${it.value}" },
    )
}

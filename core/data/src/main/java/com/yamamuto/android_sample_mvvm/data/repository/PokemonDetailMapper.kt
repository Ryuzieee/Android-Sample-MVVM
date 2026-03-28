@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import kotlinx.serialization.InternalSerializationApi

/** DTO → Domain */
internal fun PokemonDetailResponse.toDomain(): PokemonDetail =
    PokemonDetail(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        types = types.map { it.type.name },
        abilities = abilities.map {
            PokemonDetail.Ability(name = it.ability.name, isHidden = it.isHidden)
        },
        imageUrl = sprites.other.officialArtwork.frontDefault,
        stats = stats.map { PokemonDetail.Stat(name = it.stat.name, value = it.baseStat) },
    )

/** Entity → Domain */
internal fun PokemonDetailEntity.toDomain(): PokemonDetail =
    PokemonDetail(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        types = types.split(","),
        abilities = abilities.split(";").mapNotNull { entry ->
            val parts = entry.split(":")
            when (parts.size) {
                3 -> PokemonDetail.Ability(parts[0], parts[1], parts[2].toBooleanStrictOrNull() ?: false)
                2 -> PokemonDetail.Ability(parts[0], "", parts[1].toBooleanStrictOrNull() ?: false)
                else -> null
            }
        },
        imageUrl = imageUrl,
        stats = stats.split(";").mapNotNull { entry ->
            val parts = entry.split(":")
            if (parts.size == 2) PokemonDetail.Stat(parts[0], parts[1].toInt()) else null
        },
    )

/** Domain → Entity */
internal fun PokemonDetail.toEntity(): PokemonDetailEntity =
    PokemonDetailEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        types = types.joinToString(","),
        abilities = abilities.joinToString(";") { "${it.name}:${it.japaneseName}:${it.isHidden}" },
        imageUrl = imageUrl,
        stats = stats.joinToString(";") { "${it.name}:${it.value}" },
    )

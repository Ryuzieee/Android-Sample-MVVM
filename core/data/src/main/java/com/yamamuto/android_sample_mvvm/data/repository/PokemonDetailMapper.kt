@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.local.entity.AbilityEntry
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity
import com.yamamuto.android_sample_mvvm.data.local.entity.StatEntry
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import kotlinx.serialization.InternalSerializationApi

/** DTO → Entity */
internal fun PokemonDetailResponse.toEntity(): PokemonDetailEntity {
    return PokemonDetailEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        types = types.map { it.type.name },
        abilities = abilities.map { AbilityEntry(name = it.ability.name, isHidden = it.isHidden) },
        imageUrl = sprites.other.officialArtwork.frontDefault,
        stats = stats.map { StatEntry(name = it.stat.name, value = it.baseStat) },
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
        types = types,
        abilities = abilities.map {
            PokemonDetailModel.Ability(name = it.name, japaneseName = it.japaneseName, isHidden = it.isHidden)
        },
        imageUrl = imageUrl,
        stats = stats.map { PokemonDetailModel.Stat(name = it.name, value = it.value) },
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
        types = types,
        abilities = abilities.map { AbilityEntry(name = it.name, japaneseName = it.japaneseName, isHidden = it.isHidden) },
        imageUrl = imageUrl,
        stats = stats.map { StatEntry(name = it.name, value = it.value) },
    )
}

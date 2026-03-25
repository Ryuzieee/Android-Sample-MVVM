package com.yamamuto.android_sample_mvvm.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ポケモン詳細APIレスポンスのDTO。
 *
 * ドメインモデルへのマッピングは [PokemonRepositoryImpl] で行う。
 */
@Serializable
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlot>,
    val sprites: Sprites,
    val stats: List<StatSlot>,
) {
    @Serializable
    data class TypeSlot(
        val type: TypeInfo,
    )

    @Serializable
    data class TypeInfo(
        val name: String,
    )

    @Serializable
    data class Sprites(
        val other: Other,
    ) {
        @Serializable
        data class Other(
            @SerialName("official-artwork")
            val officialArtwork: OfficialArtwork,
        ) {
            @Serializable
            data class OfficialArtwork(
                @SerialName("front_default")
                val frontDefault: String,
            )
        }
    }

    @Serializable
    data class StatSlot(
        @SerialName("base_stat") val baseStat: Int,
        val stat: StatInfo,
    )

    @Serializable
    data class StatInfo(
        val name: String,
    )
}

package com.yamamuto.android_sample_mvvm.data.api.dto

import com.google.gson.annotations.SerializedName

/**
 * ポケモン詳細APIレスポンスのDTO。
 *
 * ドメインモデルへのマッピングは [PokemonRepositoryImpl] で行う。
 */
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlot>,
    val sprites: Sprites,
    val stats: List<StatSlot>,
) {
    data class TypeSlot(val type: TypeInfo)

    data class TypeInfo(val name: String)

    data class Sprites(val other: Other) {
        data class Other(
            @SerializedName("official-artwork")
            val officialArtwork: OfficialArtwork,
        ) {
            data class OfficialArtwork(
                @SerializedName("front_default")
                val frontDefault: String,
            )
        }
    }

    data class StatSlot(
        @SerializedName("base_stat") val baseStat: Int,
        val stat: StatInfo,
    )

    data class StatInfo(val name: String)
}

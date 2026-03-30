@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.api.response.PokemonListResponse
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonNameEntity
import kotlinx.serialization.InternalSerializationApi

/** DTO → Entity リスト。 */
internal fun PokemonListResponse.toEntity(): List<PokemonNameEntity> {
    return results.map { PokemonNameEntity(name = it.name) }
}

/** Entity リスト → クエリであいまい検索した結果。 */
internal fun List<PokemonNameEntity>.toModel(query: String): List<String> {
    return map { it.name }.filter { it.contains(query.trim(), ignoreCase = true) }
}

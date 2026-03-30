@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.api.response.AbilityResponse
import kotlinx.serialization.InternalSerializationApi

/** Ability DTO → Model（language → name マップ）。 */
internal fun AbilityResponse.toModel(): Map<String, String> {
    return names.associate { it.language.name to it.name }
}

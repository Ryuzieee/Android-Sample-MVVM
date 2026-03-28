@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.api.dto.AbilityResponse
import kotlinx.serialization.InternalSerializationApi

/** Ability DTO → ローカライズ名マップ（language → name）。 */
internal fun AbilityResponse.toLocalizedNames(): Map<String, String> =
    names.associate { it.language.name to it.name }

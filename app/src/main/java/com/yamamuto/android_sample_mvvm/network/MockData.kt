package com.yamamuto.android_sample_mvvm.network

import com.yamamuto.android_sample_mvvm.network.mock.buildMockAbilityResponse
import com.yamamuto.android_sample_mvvm.network.mock.buildMockDetailResponse
import com.yamamuto.android_sample_mvvm.network.mock.buildMockEvolutionResponse
import com.yamamuto.android_sample_mvvm.network.mock.buildMockListResponse
import com.yamamuto.android_sample_mvvm.network.mock.buildMockSpeciesResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * モックフレーバー用レスポンスデータのファサード。
 *
 * 各レスポンスは実際の DTO クラスを構築し [Json.encodeToString] でシリアライズするため、
 * 本物の API レスポンスと同じ JSON 構造が保証される。
 */
@Singleton
class MockData
    @Inject
    constructor(
        private val json: Json,
    ) {
        fun pokemonList(): String = buildMockListResponse(json)

        fun pokemonDetail(name: String): String = buildMockDetailResponse(json, name)

        fun pokemonSpecies(name: String): String = buildMockSpeciesResponse(json, name)

        fun evolutionChain(chainId: Int): String = buildMockEvolutionResponse(json, chainId)

        fun ability(name: String): String = buildMockAbilityResponse(json, name)
    }

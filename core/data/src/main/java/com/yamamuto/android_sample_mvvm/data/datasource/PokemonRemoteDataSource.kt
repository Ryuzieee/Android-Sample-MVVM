@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.datasource

import com.yamamuto.android_sample_mvvm.data.api.PokeApiService
import com.yamamuto.android_sample_mvvm.data.api.dto.AbilityResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonListResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

/**
 * PokeAPI からデータを取得するリモートデータソース。
 *
 * APIの呼び出しのみを担当し、データ変換は行わない。
 */
class PokemonRemoteDataSource @Inject constructor(
    private val api: PokeApiService,
) {
    suspend fun getPokemonList(limit: Int, offset: Int): PokemonListResponse {
        return api.getPokemonList(limit, offset)
    }

    suspend fun getPokemonDetail(name: String): PokemonDetailResponse {
        return api.getPokemonDetail(name)
    }

    suspend fun getPokemonSpecies(name: String): PokemonSpeciesResponse {
        return api.getPokemonSpecies(name)
    }

    suspend fun getEvolutionChain(url: String): EvolutionChainResponse {
        return api.getEvolutionChain(url)
    }

    suspend fun getAbility(name: String): AbilityResponse {
        return api.getAbility(name)
    }
}

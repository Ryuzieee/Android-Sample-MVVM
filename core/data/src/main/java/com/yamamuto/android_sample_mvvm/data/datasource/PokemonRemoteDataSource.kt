@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.datasource

import com.yamamuto.android_sample_mvvm.data.api.PokeApiService
import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonListResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse
import kotlinx.serialization.InternalSerializationApi

/**
 * PokeAPI からデータを取得するリモートデータソース。
 *
 * APIの呼び出しのみを担当し、データ変換は行わない。
 */
class PokemonRemoteDataSource(
    private val api: PokeApiService,
) {
    suspend fun getPokemonList(
        limit: Int,
        offset: Int,
    ): PokemonListResponse = api.getPokemonList(limit, offset)

    suspend fun getPokemonDetail(name: String): PokemonDetailResponse = api.getPokemonDetail(name)

    suspend fun getPokemonSpecies(name: String): PokemonSpeciesResponse = api.getPokemonSpecies(name)

    suspend fun getEvolutionChain(url: String): EvolutionChainResponse = api.getEvolutionChain(url)
}

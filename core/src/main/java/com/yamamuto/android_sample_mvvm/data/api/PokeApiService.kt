@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.api

import com.yamamuto.android_sample_mvvm.data.api.dto.AbilityResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.EvolutionChainResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonListResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonSpeciesResponse
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * PokeAPI の REST エンドポイント定義。
 *
 * ベースURL: https://pokeapi.co/api/v2/
 */
interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String,
    ): PokemonDetailResponse

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpecies(
        @Path("name") name: String,
    ): PokemonSpeciesResponse

    @GET
    suspend fun getEvolutionChain(
        @Url url: String,
    ): EvolutionChainResponse

    @GET("ability/{name}")
    suspend fun getAbility(
        @Path("name") name: String,
    ): AbilityResponse
}

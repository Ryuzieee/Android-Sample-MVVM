package com.yamamuto.android_sample_mvvm.data.datasource

import com.yamamuto.android_sample_mvvm.data.api.PokeApiService
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonDetailResponse
import com.yamamuto.android_sample_mvvm.data.api.dto.PokemonListResponse

class PokemonRemoteDataSource(private val api: PokeApiService) {

    suspend fun getPokemonList(limit: Int, offset: Int): PokemonListResponse =
        api.getPokemonList(limit, offset)

    suspend fun getPokemonDetail(name: String): PokemonDetailResponse =
        api.getPokemonDetail(name)
}

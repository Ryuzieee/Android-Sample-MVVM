package com.yamamuto.android_sample_mvvm.domain.repository

import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel

/** ポケモンデータへのアクセスを抽象化するリポジトリインターフェース。 */
interface PokemonRepository {
    suspend fun getPokemonDetail(
        name: String,
        forceRefresh: Boolean = false,
    ): Result<PokemonDetailModel>

    suspend fun getPokemonSpecies(name: String): Result<PokemonSpeciesModel>

    suspend fun getEvolutionChainByUrl(url: String): Result<List<EvolutionStageModel>>

    suspend fun getAbilityLocalizedNames(name: String): Result<Map<String, String>>

    suspend fun searchPokemonNames(query: String): Result<List<String>>

    suspend fun getPokemonList(
        offset: Int,
        limit: Int,
    ): Result<List<PokemonSummaryModel>>
}

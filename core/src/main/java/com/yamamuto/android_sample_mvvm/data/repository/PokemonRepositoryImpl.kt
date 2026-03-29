@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.mapper.toEntities
import com.yamamuto.android_sample_mvvm.data.mapper.toEntity
import com.yamamuto.android_sample_mvvm.data.mapper.toModel
import com.yamamuto.android_sample_mvvm.data.util.handleRemote
import com.yamamuto.android_sample_mvvm.data.util.handleWithCache
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

private const val POKEMON_LIST_LIMIT = 2000

/**
 * [PokemonRepository] の実装クラス。
 *
 * キャッシュ付きは [handleWithCache]、API のみは [handleRemote] を使い、
 * 例外を [Result.failure] に変換する。
 */
class PokemonRepositoryImpl @Inject constructor(
    private val dataSource: PokemonRemoteDataSource,
    private val dao: PokemonDao,
) : PokemonRepository {
    override suspend fun getPokemonDetail(
        name: String,
        forceRefresh: Boolean,
    ): Result<PokemonDetailModel> {
        return handleWithCache(
            forceRefresh = forceRefresh,
            load = { dao.getPokemonDetail(name) },
            fetch = { dataSource.getPokemonDetail(name).toEntity() },
            toModel = { it.toModel() },
            cachedAt = { it.cachedAt },
            save = { dao.insertPokemonDetail(it) },
        )
    }

    override suspend fun getPokemonSpecies(name: String): Result<PokemonSpeciesModel> {
        return handleRemote(
            fetch = { dataSource.getPokemonSpecies(name) },
            toModel = { it.toModel() },
        )
    }

    override suspend fun getEvolutionChainByUrl(url: String): Result<List<EvolutionStageModel>> {
        return handleRemote(
            fetch = { dataSource.getEvolutionChain(url) },
            toModel = { it.toModel() },
        )
    }

    override suspend fun getAbilityLocalizedNames(name: String): Result<Map<String, String>> {
        return handleRemote(
            fetch = { dataSource.getAbility(name) },
            toModel = { it.toModel() },
        )
    }

    override suspend fun searchPokemonNames(query: String): Result<List<String>> {
        return handleWithCache(
            load = { dao.getAllPokemonNames().takeIf { it.isNotEmpty() } },
            fetch = {
                dataSource.getPokemonList(limit = POKEMON_LIST_LIMIT, offset = 0).toEntities()
            },
            toModel = { it.toModel(query) },
            cachedAt = { it.first().cachedAt },
            save = { dao.insertPokemonNames(it) },
        )
    }
}

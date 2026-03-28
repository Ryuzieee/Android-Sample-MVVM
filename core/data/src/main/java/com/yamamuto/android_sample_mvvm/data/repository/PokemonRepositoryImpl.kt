@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.util.repositoryHandler
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.serialization.InternalSerializationApi

private const val POKEMON_LIST_LIMIT = 2000

/**
 * [PokemonRepository] の実装クラス。
 *
 * 全メソッドは [repositoryHandler] を使い、例外を [Result.failure] に変換する。
 */
class PokemonRepositoryImpl(
    private val dataSource: PokemonRemoteDataSource,
    private val dao: PokemonDao,
) : PokemonRepository {

    override suspend fun getPokemonDetail(
        name: String,
        forceRefresh: Boolean
    ): Result<PokemonDetailModel> {
        return repositoryHandler(
            forceRefresh = forceRefresh,
            load = { dao.getPokemonDetail(name) },
            fetch = { dataSource.getPokemonDetail(name).toEntity() },
            toModel = { it.toDomain() },
            cachedAt = { it.cachedAt },
            save = { dao.insertPokemonDetail(it) },
        )
    }

    override suspend fun getPokemonSpecies(name: String): Result<PokemonSpeciesModel> {
        return repositoryHandler(
            fetch = { dataSource.getPokemonSpecies(name) },
            toModel = { it.toDomain() },
        )
    }

    override suspend fun getEvolutionChainByUrl(url: String): Result<List<EvolutionStageModel>> {
        return repositoryHandler(
            fetch = { dataSource.getEvolutionChain(url) },
            toModel = { it.toStages() },
        )
    }

    override suspend fun getAbilityLocalizedNames(name: String): Result<Map<String, String>> {
        return repositoryHandler(
            fetch = { dataSource.getAbility(name) },
            toModel = { it.toLocalizedNames() },
        )
    }

    override suspend fun searchPokemonNames(query: String): Result<List<String>> {
        return repositoryHandler(
            load = { dao.getAllPokemonNames().takeIf { it.isNotEmpty() } },
            fetch = {
                dataSource.getPokemonList(limit = POKEMON_LIST_LIMIT, offset = 0).toEntities()
            },
            toModel = { it.toModels(query) },
            cachedAt = { it.first().cachedAt },
            save = { dao.insertPokemonNames(it) },
        )
    }
}

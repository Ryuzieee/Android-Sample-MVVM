@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonNameEntity
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

    override suspend fun getPokemonDetail(name: String, forceRefresh: Boolean): Result<PokemonDetailModel> {
        return repositoryHandler(
            forceRefresh = forceRefresh,
            local = { dao.getPokemonDetail(name)?.takeUnless { it.isExpired() }?.toDomain() },
            remote = { dataSource.getPokemonDetail(name).toDomain() },
            cache = { dao.insertPokemonDetail(it.toEntity()) },
        )
    }

    override suspend fun getPokemonSpecies(name: String): Result<PokemonSpeciesModel> {
        return repositoryHandler(
            remote = { dataSource.getPokemonSpecies(name).toDomain() },
        )
    }

    override suspend fun getEvolutionChainByUrl(url: String): Result<List<EvolutionStageModel>> {
        return repositoryHandler(
            remote = { dataSource.getEvolutionChain(url).toStages() },
        )
    }

    override suspend fun getAbilityLocalizedNames(name: String): Result<Map<String, String>> {
        return repositoryHandler(
            remote = { dataSource.getAbility(name).toLocalizedNames() },
        )
    }

    override suspend fun searchPokemonNames(query: String): Result<List<String>> {
        return repositoryHandler(
            local = { dao.getAllPokemonNames().takeIf { it.isNotEmpty() } },
            remote = {
                dataSource.getPokemonList(limit = POKEMON_LIST_LIMIT, offset = 0)
                    .results.map { it.name }
            },
            cache = { names -> dao.insertPokemonNames(names.map { PokemonNameEntity(name = it) }) },
        ).map { names ->
            names.filter { it.contains(query.trim(), ignoreCase = true) }
        }
    }
}

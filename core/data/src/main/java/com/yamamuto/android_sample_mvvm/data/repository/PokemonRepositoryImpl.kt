@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonNameEntity
import com.yamamuto.android_sample_mvvm.data.util.cachedApiCall
import com.yamamuto.android_sample_mvvm.data.util.safeApiCall
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.serialization.InternalSerializationApi

private const val CACHE_DURATION_MS = 5 * 60 * 1000L // 5分
private const val POKEMON_LIST_LIMIT = 2000

/**
 * [PokemonRepository] の実装クラス。
 *
 * ネットワーク取得後に Room キャッシュに保存し、
 * オフライン時はキャッシュからデータを返す local-first パターン。
 */
class PokemonRepositoryImpl(
    private val dataSource: PokemonRemoteDataSource,
    private val dao: PokemonDao,
) : PokemonRepository {

    override suspend fun getPokemonDetail(name: String, forceRefresh: Boolean): Result<PokemonDetailModel> {
        return safeApiCall {
            cachedApiCall(
                forceRefresh = forceRefresh,
                fromCache = {
                    val cached = dao.getPokemonDetail(name)
                    if (cached != null && System.currentTimeMillis() - cached.cachedAt < CACHE_DURATION_MS) {
                        cached.toDomain()
                    } else null
                },
                fromNetwork = { dataSource.getPokemonDetail(name).toDomain() },
                saveToCache = { dao.insertPokemonDetail(it.toEntity()) },
            )
        }
    }

    override suspend fun getPokemonSpecies(name: String): Result<PokemonSpeciesModel> {
        return safeApiCall { dataSource.getPokemonSpecies(name).toDomain() }
    }

    override suspend fun getEvolutionChainByUrl(url: String): Result<List<EvolutionStageModel>> {
        return safeApiCall { dataSource.getEvolutionChain(url).toStages() }
    }

    override suspend fun getAbilityLocalizedNames(name: String): Result<Map<String, String>> {
        return safeApiCall { dataSource.getAbility(name).toLocalizedNames() }
    }

    override suspend fun searchPokemonNames(query: String): Result<List<String>> {
        return safeApiCall {
            val names = loadPokemonNames()
            names.filter { it.contains(query.trim(), ignoreCase = true) }
        }
    }

    private suspend fun loadPokemonNames(): List<String> {
        val cached = dao.getAllPokemonNames()
        if (cached.isNotEmpty()) {
            return cached
        }
        val names = dataSource.getPokemonList(limit = POKEMON_LIST_LIMIT, offset = 0)
            .results.map { it.name }
        dao.insertPokemonNames(names.map { PokemonNameEntity(name = it) })
        return names
    }
}

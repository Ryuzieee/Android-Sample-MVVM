@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonNameEntity
import com.yamamuto.android_sample_mvvm.data.util.safeApiCall
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStageModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpeciesModel
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.serialization.InternalSerializationApi

private const val POKEMON_LIST_LIMIT = 2000

/**
 * [PokemonRepository] の実装クラス。
 *
 * 全メソッドは [safeApiCall] でラップし、例外を [Result.failure] に変換する。
 * キャッシュが必要な場合は DB を直接確認してから API を呼ぶ。
 */
class PokemonRepositoryImpl(
    private val dataSource: PokemonRemoteDataSource,
    private val dao: PokemonDao,
) : PokemonRepository {

    override suspend fun getPokemonDetail(name: String, forceRefresh: Boolean): Result<PokemonDetailModel> {
        return safeApiCall {
            if (!forceRefresh) {
                val cached = dao.getPokemonDetail(name)
                if (cached != null && !cached.isExpired()) {
                    return@safeApiCall cached.toDomain()
                }
            }
            val detail = dataSource.getPokemonDetail(name).toDomain()
            dao.insertPokemonDetail(detail.toEntity())
            detail
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

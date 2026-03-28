@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.paging.PokemonPagingSource
import com.yamamuto.android_sample_mvvm.data.util.cachedApiCall
import com.yamamuto.android_sample_mvvm.data.util.safeApiCall
import com.yamamuto.android_sample_mvvm.domain.model.EvolutionStage
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSpecies
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi

private const val CACHE_DURATION_MS = 5 * 60 * 1000L // 5分

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
    private var cachedPokemonNames: List<String>? = null

    override fun getPokemonPagingData(): Flow<PagingData<Pokemon>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { PokemonPagingSource(dataSource) },
        ).flow

    override suspend fun getPokemonDetail(name: String, forceRefresh: Boolean): PokemonDetail =
        safeApiCall {
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

    override suspend fun getPokemonSpecies(name: String): PokemonSpecies =
        safeApiCall { dataSource.getPokemonSpecies(name).toDomain() }

    override suspend fun getEvolutionChain(name: String): List<EvolutionStage> =
        safeApiCall {
            val species = dataSource.getPokemonSpecies(name)
            val chain = dataSource.getEvolutionChain(species.evolutionChain.url)

            // 各ステージの日本語名を並列取得
            val stagesRaw = chain.toStages()
            val japaneseNames = coroutineScope {
                stagesRaw.map { stage ->
                    async {
                        runCatching { dataSource.getPokemonSpecies(stage.name).extractJapaneseName() }
                            .getOrDefault("")
                    }
                }.awaitAll()
            }
            val nameMap = stagesRaw.zip(japaneseNames).associate { (stage, jaName) -> stage.name to jaName }
            chain.toStages(nameMap)
        }

    override suspend fun getAbilityJapaneseName(name: String): String =
        safeApiCall {
            val response = dataSource.getAbility(name)
            response.names.firstOrNull { it.language.name == "ja" }?.name
                ?: response.names.firstOrNull { it.language.name == "ja-hrkt" }?.name
                ?: name
        }

    override suspend fun searchPokemonNames(query: String): List<String> {
        val names = cachedPokemonNames ?: safeApiCall {
            dataSource.getPokemonList(limit = 2000, offset = 0).results.map { it.name }
        }.also { cachedPokemonNames = it }
        return names.filter { it.contains(query.trim(), ignoreCase = true) }
    }
}

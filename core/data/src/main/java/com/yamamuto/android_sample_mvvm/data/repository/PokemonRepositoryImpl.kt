@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity
import com.yamamuto.android_sample_mvvm.data.paging.PokemonPagingSource
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import retrofit2.HttpException
import java.io.IOException

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
    override fun getPokemonPagingData(): Flow<PagingData<Pokemon>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { PokemonPagingSource(dataSource) },
        ).flow

    override suspend fun getPokemonDetail(name: String): PokemonDetail {
        // キャッシュが有効ならキャッシュから返す
        val cached = dao.getPokemonDetail(name)
        if (cached != null && System.currentTimeMillis() - cached.cachedAt < CACHE_DURATION_MS) {
            return cached.toDomain()
        }

        // ネットワークから取得してキャッシュに保存
        return wrapException {
            val dto = dataSource.getPokemonDetail(name)
            val detail =
                PokemonDetail(
                    id = dto.id,
                    name = dto.name,
                    height = dto.height,
                    weight = dto.weight,
                    types = dto.types.map { it.type.name },
                    imageUrl = dto.sprites.other.officialArtwork.frontDefault,
                    stats = dto.stats.map { PokemonDetail.Stat(name = it.stat.name, value = it.baseStat) },
                )
            dao.insertPokemonDetail(detail.toEntity())
            detail
        }
    }

    private fun PokemonDetailEntity.toDomain(): PokemonDetail =
        PokemonDetail(
            id = id,
            name = name,
            height = height,
            weight = weight,
            types = types.split(","),
            imageUrl = imageUrl,
            stats =
                stats.split(";").mapNotNull { entry ->
                    val parts = entry.split(":")
                    if (parts.size == 2) PokemonDetail.Stat(parts[0], parts[1].toInt()) else null
                },
        )

    private fun PokemonDetail.toEntity(): PokemonDetailEntity =
        PokemonDetailEntity(
            id = id,
            name = name,
            height = height,
            weight = weight,
            types = types.joinToString(","),
            imageUrl = imageUrl,
            stats = stats.joinToString(";") { "${it.name}:${it.value}" },
        )

    private inline fun <T> wrapException(block: () -> T): T =
        try {
            block()
        } catch (e: IOException) {
            throw AppException.Network(e)
        } catch (e: HttpException) {
            throw AppException.Server(e.code(), e)
        } catch (e: Exception) {
            throw AppException.Unknown(e)
        }
}

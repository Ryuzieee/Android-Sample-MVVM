package com.yamamuto.android_sample_mvvm.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.paging.PokemonPagingSource
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

/**
 * [PokemonRepository] の実装クラス。
 *
 * [PokemonRemoteDataSource] から取得したDTOをドメインモデルに変換して返す。
 * ネットワークエラーを [AppException] に分類する。
 */
class PokemonRepositoryImpl(
    private val dataSource: PokemonRemoteDataSource,
) : PokemonRepository {
    override suspend fun getPokemonList(
        limit: Int,
        offset: Int,
    ): List<Pokemon> =
        wrapException {
            dataSource.getPokemonList(limit, offset).results.map { dto ->
                Pokemon(name = dto.name, url = dto.url)
            }
        }

    override fun getPokemonPagingData(): Flow<PagingData<Pokemon>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { PokemonPagingSource(dataSource) },
        ).flow

    override suspend fun getPokemonDetail(name: String): PokemonDetail =
        wrapException {
            val dto = dataSource.getPokemonDetail(name)
            PokemonDetail(
                id = dto.id,
                name = dto.name,
                height = dto.height,
                weight = dto.weight,
                types = dto.types.map { it.type.name },
                imageUrl = dto.sprites.other.officialArtwork.frontDefault,
                stats = dto.stats.map { PokemonDetail.Stat(name = it.stat.name, value = it.baseStat) },
            )
        }

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

@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import kotlinx.serialization.InternalSerializationApi

/**
 * ポケモン一覧の Paging データソース。
 *
 * PokeAPI の offset/limit ベースのページネーションに対応する。
 */
class PokemonPagingSource(
    private val dataSource: PokemonRemoteDataSource,
) : PagingSource<Int, Pokemon>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> =
        try {
            val offset = params.key ?: 0
            val response = dataSource.getPokemonList(limit = params.loadSize, offset = offset)
            val pokemons = response.results.map { Pokemon(name = it.name, url = it.url) }
            LoadResult.Page(
                data = pokemons,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = if (pokemons.isEmpty()) null else offset + params.loadSize,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey
        }
}

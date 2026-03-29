@file:OptIn(InternalSerializationApi::class)

package com.yamamuto.android_sample_mvvm.data.paging

import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

/** ポケモン一覧用の [PagingSourceFactory]。 */
class PokemonPagingSourceFactory @Inject constructor(
    private val dataSource: PokemonRemoteDataSource,
) : PagingSourceFactory<PokemonSummaryModel> {
    override fun create(): OffsetPagingSource<PokemonSummaryModel> {
        return OffsetPagingSource { offset, limit ->
            dataSource.getPokemonList(limit = limit, offset = offset)
                .results.map { PokemonSummaryModel(name = it.name, url = it.url) }
        }
    }
}

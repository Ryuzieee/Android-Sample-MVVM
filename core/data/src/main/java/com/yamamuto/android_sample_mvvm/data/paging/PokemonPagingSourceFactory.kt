package com.yamamuto.android_sample_mvvm.data.paging

import androidx.paging.PagingSource
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.domain.model.PokemonSummaryModel
import javax.inject.Inject

/** [PokemonPagingSource] のファクトリ。VM に注入して Pager を構築するために使用する。 */
class PokemonPagingSourceFactory
    @Inject
    constructor(
        private val dataSource: PokemonRemoteDataSource,
    ) {
        fun create(): PagingSource<Int, PokemonSummaryModel> {
            return PokemonPagingSource(dataSource)
        }
    }

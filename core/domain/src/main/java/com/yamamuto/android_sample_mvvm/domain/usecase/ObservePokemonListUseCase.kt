package com.yamamuto.android_sample_mvvm.domain.usecase

import androidx.paging.PagingData
import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** ポケモン一覧を監視するユースケース。 */
class ObservePokemonListUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        operator fun invoke(): Flow<PagingData<Pokemon>> = repository.observePokemonPaging()
    }

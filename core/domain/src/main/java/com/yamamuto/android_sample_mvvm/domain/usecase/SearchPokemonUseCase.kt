package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import javax.inject.Inject

/** ポケモン名であいまい検索するユースケース。 */
class SearchPokemonUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        suspend operator fun invoke(query: String): Result<List<String>> =
            repository.searchPokemonNames(query)
    }

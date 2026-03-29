package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import com.yamamuto.android_sample_mvvm.domain.util.LanguageCodes.japaneseName
import javax.inject.Inject

/** 特性の日本語名を取得するユースケース。 */
class GetAbilityJapaneseNameUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        suspend operator fun invoke(name: String): Result<String> {
            return repository.getAbilityLocalizedNames(name).map { names ->
                names.japaneseName(fallback = name)
            }
        }
    }

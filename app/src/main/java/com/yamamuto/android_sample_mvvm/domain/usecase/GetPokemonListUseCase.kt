package com.yamamuto.android_sample_mvvm.domain.usecase

import com.yamamuto.android_sample_mvvm.domain.model.Pokemon
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import javax.inject.Inject

/**
 * ポケモン一覧を取得するユースケース。
 *
 * 1ユースケース = 1メソッドの原則に基づき、[invoke] のみを公開する。
 */
class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository,
) {
    suspend operator fun invoke(limit: Int = 20, offset: Int = 0): List<Pokemon> =
        repository.getPokemonList(limit, offset)
}

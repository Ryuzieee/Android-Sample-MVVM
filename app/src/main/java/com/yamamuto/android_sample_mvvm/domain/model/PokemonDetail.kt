package com.yamamuto.android_sample_mvvm.domain.model

/**
 * ポケモン詳細画面で表示するデータを表すドメインモデル。
 *
 * ステータス情報は [Stat] のリストとして保持する。
 */
data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val imageUrl: String,
    val stats: List<Stat>,
) {
    /** ポケモンの各種基本ステータス（HP・攻撃力など）。 */
    data class Stat(val name: String, val value: Int)
}

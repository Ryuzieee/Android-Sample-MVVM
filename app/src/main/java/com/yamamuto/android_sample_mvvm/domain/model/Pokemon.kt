package com.yamamuto.android_sample_mvvm.domain.model

/**
 * ポケモンの一覧表示に必要な最小限のデータを表すドメインモデル。
 *
 * [id] や [imageUrl] は [url] から導出されるプロパティ。
 */
data class Pokemon(
    val name: String,
    val url: String,
) {
    val id: Int get() = url.trimEnd('/').split('/').last().toInt()
    val imageUrl: String get() =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

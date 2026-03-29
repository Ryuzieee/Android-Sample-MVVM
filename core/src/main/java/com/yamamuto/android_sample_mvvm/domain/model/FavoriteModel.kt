package com.yamamuto.android_sample_mvvm.domain.model

/** お気に入り登録されたポケモンを表すドメインモデル。 */
data class FavoriteModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
)

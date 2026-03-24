package com.yamamuto.android_sample_mvvm.domain.model

data class Pokemon(
    val name: String,
    val url: String,
) {
    val id: Int get() = url.trimEnd('/').split('/').last().toInt()
    val imageUrl: String get() =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

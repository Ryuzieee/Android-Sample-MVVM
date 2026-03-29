package com.yamamuto.android_sample_mvvm.data.mapper

import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import com.yamamuto.android_sample_mvvm.domain.model.FavoriteModel
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel

/** Entity → Model */
internal fun FavoriteEntity.toModel(): FavoriteModel {
    return FavoriteModel(id = id, name = name, imageUrl = imageUrl)
}

/** Model → Entity */
internal fun PokemonDetailModel.toEntity(): FavoriteEntity {
    return FavoriteEntity(id = id, name = name, imageUrl = imageUrl)
}

package com.yamamuto.android_sample_mvvm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/** お気に入りポケモンの CRUD 操作を行う DAO。 */
@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY savedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT COUNT(*) > 0 FROM favorites WHERE id = :id")
    fun isFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavorite(id: Int)
}

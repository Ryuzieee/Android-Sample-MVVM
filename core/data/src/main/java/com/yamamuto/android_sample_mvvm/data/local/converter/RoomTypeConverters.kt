package com.yamamuto.android_sample_mvvm.data.local.converter

import androidx.room.TypeConverter
import com.yamamuto.android_sample_mvvm.data.local.entity.AbilityEntry
import com.yamamuto.android_sample_mvvm.data.local.entity.StatEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Room 用 TypeConverter。kotlinx.serialization で JSON 変換する。 */
class RoomTypeConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = json.decodeFromString(value)

    @TypeConverter
    fun fromAbilityList(value: List<AbilityEntry>): String = json.encodeToString(value)

    @TypeConverter
    fun toAbilityList(value: String): List<AbilityEntry> = json.decodeFromString(value)

    @TypeConverter
    fun fromStatList(value: List<StatEntry>): String = json.encodeToString(value)

    @TypeConverter
    fun toStatList(value: String): List<StatEntry> = json.decodeFromString(value)
}

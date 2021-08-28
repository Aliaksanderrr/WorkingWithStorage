package rs.android.task4.repository.database

import androidx.room.TypeConverter
import java.util.*

class CatTypeConverters {
    @TypeConverter
    fun fromDate(date: Date?): Long?{
        return date?.time
    }

    @TypeConverter
    fun toDate(millis: Long?): Date?{
        return millis?.let { Date(it) }
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String?{
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID?{
        return UUID.fromString(uuid)
    }
}
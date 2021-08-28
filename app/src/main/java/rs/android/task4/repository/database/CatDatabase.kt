package rs.android.task4.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rs.android.task4.data.Cat

@Database(entities = [Cat::class], version = 1,exportSchema = false)
@TypeConverters(CatTypeConverters::class)
abstract class CatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao

}
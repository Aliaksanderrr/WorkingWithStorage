package rs.android.task4.repository.databaseRoom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SimpleSQLiteQuery
import rs.android.task4.TAG
import rs.android.task4.data.Cat
import rs.android.task4.repository.RepositoryDAO
import java.util.*

@Database(entities = [Cat::class], version = 1,exportSchema = false)
@TypeConverters(CatTypeConverters::class)
abstract class CatDatabase : RepositoryDAO, RoomDatabase() {

    abstract fun catDao(): CatDao

    override fun getCats(filter: String): LiveData<List<Cat>> {
        Log.d(TAG, "CatDatabase.getCats(): $filter")
        var query = SimpleSQLiteQuery("SELECT * FROM cat ORDER BY $filter")
        return catDao().getCats(query)
    }

    override fun getCat(id: UUID): LiveData<Cat?> = catDao().getCat(id)

    override fun addCat(cat: Cat) = catDao().addCat(cat)

    override fun updateCat(cat: Cat) = catDao().updateCat(cat)

    override fun deleteCat(cat: Cat) = catDao().deleteCat(cat)

}
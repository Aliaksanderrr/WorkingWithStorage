package rs.android.task4.repository.databaseRoom

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import rs.android.task4.data.Cat
import java.util.*

@Dao
interface CatDao{

    @RawQuery(observedEntities = [Cat::class])
    fun getCats(query: SupportSQLiteQuery): LiveData<List<Cat>>

    @Query("SELECT * FROM cat WHERE id=(:id)")
    fun getCat(id: UUID): LiveData<Cat?>

    @Insert
    fun addCat(cat: Cat)

    @Update
    fun updateCat(cat: Cat)

    @Delete
    fun deleteCat(cat: Cat)

}
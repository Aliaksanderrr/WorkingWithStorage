package rs.android.task4.repository.databaseRoom

import androidx.lifecycle.LiveData
import androidx.room.*
import rs.android.task4.data.Cat
import rs.android.task4.repository.RepositoryDAO
import java.util.*

@Dao
interface CatDao{

    @Query("SELECT * FROM cat")
    fun getCats(): LiveData<List<Cat>>

    @Query("SELECT * FROM cat WHERE id=(:id)")
    fun getCat(id: UUID): LiveData<Cat?>

    @Insert
    fun addCat(cat: Cat)

    @Update
    fun updateCat(cat: Cat)

    @Delete
    fun deleteCat(cat: Cat)

}
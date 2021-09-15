package rs.android.task4.repository

import androidx.lifecycle.LiveData
import rs.android.task4.data.Cat
import java.util.*

interface RepositoryDAO {

    fun getCats(filter: String): LiveData<List<Cat>>

    fun getCat(id: UUID): LiveData<Cat?>

    fun addCat(cat: Cat)

    fun updateCat(cat: Cat)

    fun deleteCat(cat: Cat)

}
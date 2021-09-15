package rs.android.task4.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.room.Room
import rs.android.task4.TAG
import rs.android.task4.data.Cat
import rs.android.task4.repository.databaseCursor.CatSQLiteOpenHelper
import rs.android.task4.repository.databaseRoom.CatDatabase
import java.util.concurrent.Executors

const val DATABASE_NAME = "CatsDatabase"

class Repository private constructor(appContext: Context) {

//    private val database: RepositoryDAO = Room.databaseBuilder(appContext, CatDatabase::class.java, DATABASE_NAME)
//                                            .build()

    private val database: RepositoryDAO = CatSQLiteOpenHelper(appContext, DATABASE_NAME)

    private val executor = Executors.newSingleThreadExecutor()

    private val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun getCats(): LiveData<List<Cat>>{
       val filter: String = preferences.getString("filter_field", "id").toString()
        Log.d("filter", "Preferen: $filter")
        return database.getCats(filter)
    }

    fun addCat(cat: Cat) {
        executor.execute {
            database.addCat(cat)
        }
    }

    fun updateCat(cat: Cat){
        executor.execute {
            database.updateCat(cat)
        }
    }

    fun deleteCat(cat: Cat) {
        executor.execute {
            database.deleteCat(cat)
        }
    }

    companion object{
        private var INSTANCE: Repository? = null

        fun initialize(appContext: Context){
            Log.d(TAG, "Repository companion fun initialize")
            if (INSTANCE == null){
                INSTANCE = Repository(appContext)
            }
        }

        fun get(): Repository{
            return INSTANCE ?: throw IllegalStateException("Repository mast be initialised")
        }
    }
}
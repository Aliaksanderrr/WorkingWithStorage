package rs.android.task4.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import rs.android.task4.TAG
import rs.android.task4.data.Cat
import rs.android.task4.repository.database.CatDatabase
import java.util.concurrent.Executors

private const val DATABASE_NAME = "CatsDatabase"

class Repository private constructor(appContext: Context) {

    private val database: CatDatabase = Room.databaseBuilder(appContext, CatDatabase::class.java, DATABASE_NAME)
                                            .build()
    private val executor = Executors.newSingleThreadExecutor()



    fun getCats(): LiveData<List<Cat>>{
       return database.catDao().getCats()
    }

    fun addCat(cat: Cat) {
        executor.execute {
            database.catDao().addCat(cat)
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
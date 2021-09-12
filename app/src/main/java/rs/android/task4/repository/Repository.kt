package rs.android.task4.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import rs.android.task4.TAG
import rs.android.task4.data.Cat
import rs.android.task4.repository.databaseCursor.CatSQLiteOpenHelper
import rs.android.task4.repository.databaseRoom.CatDatabase
import java.util.concurrent.Executors

const val DATABASE_NAME = "CatsDatabase"

class Repository private constructor(appContext: Context) {

//
//    private val database: CatDatabase = Room.databaseBuilder(appContext, CatDatabase::class.java, DATABASE_NAME)
//                                            .build()
//
//    private val database: RepositoryDAO = Room.databaseBuilder(appContext, CatDatabase::class.java, DATABASE_NAME)
//                                            .build()
    private val database: RepositoryDAO = CatSQLiteOpenHelper(appContext, DATABASE_NAME)

    private val executor = Executors.newSingleThreadExecutor()



    fun getCats(): LiveData<List<Cat>>{
       return database.getCats()
    }

    fun addCat(cat: Cat) {
        executor.execute {
            database.addCat(cat)
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
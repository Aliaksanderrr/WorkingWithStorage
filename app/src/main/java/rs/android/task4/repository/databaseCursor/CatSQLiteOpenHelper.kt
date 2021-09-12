package rs.android.task4.repository.databaseCursor

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.android.task4.data.Cat
import rs.android.task4.repository.Repository
import rs.android.task4.repository.RepositoryDAO
import java.util.*

private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "Cat"
private const val CAT_ID = "id"
private const val CAT_NAME = "name"
private const val CAT_BIRTHDAY = "birthday"
private const val CAT_BREED = "breed"

//Queries
private const val CREATE_TABLE_SQL =
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($CAT_ID VARCHAR(50) PRIMARY KEY, $CAT_NAME VARCHAR(50), $CAT_BIRTHDAY LONG, $CAT_BREED VARCHAR(50))"

class CatSQLiteOpenHelper(context: Context, databaseName: String): RepositoryDAO, SQLiteOpenHelper(
    context,
    databaseName,
    null,
    DATABASE_VERSION) {

    private val listCatsLiveData = MutableLiveData<List<Cat>>()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SQL)
        val cat = Cat()
        db.execSQL("INSERT INTO $TABLE_NAME ($CAT_ID, $CAT_NAME, $CAT_BIRTHDAY, $CAT_BREED) VALUES (${cat.id.toString()}, ${cat.name}, ${cat.birthday.time}, ${cat.breed});")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private fun getCursorWithCats(): Cursor{
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    override fun getCats(): LiveData<List<Cat>>{
        val listOfCats = mutableListOf<Cat>()
        getCursorWithCats().use { cursor ->
            if(cursor.moveToFirst()){
                do {
                    val id = UUID.fromString(cursor.getString(cursor.getColumnIndex(CAT_ID)))
                    val name = cursor.getString(cursor.getColumnIndex(CAT_NAME))
                    val birthday = Date(cursor.getLong(cursor.getColumnIndex(CAT_BIRTHDAY)))
                    val breed = cursor.getString(cursor.getColumnIndex(CAT_BREED))
                    listOfCats.add(Cat(id, name, birthday, breed))
                } while (cursor.moveToNext())
            }
        }
        listCatsLiveData.postValue(listOfCats)
        return listCatsLiveData
    }

    override fun getCat(id: UUID): LiveData<Cat?> {
        //TODO
        return MutableLiveData<Cat>()
    }

    override fun addCat(cat: Cat) {
        writableDatabase.use {
            val values = ContentValues().apply {
                put(CAT_ID, cat.id.toString())
                put(CAT_NAME, cat.name)
                put(CAT_BIRTHDAY, cat.birthday.time)
                put(CAT_BREED, cat.breed)
            }
            it?.insert(TABLE_NAME, null, values)
        }
        getCats()
    }

    override fun updateCat(cat: Cat) {
        writableDatabase.use {
            val values = ContentValues().apply {
                put(CAT_NAME, cat.name)
                put(CAT_BIRTHDAY, cat.birthday.time)
                put(CAT_BREED, cat.breed)
            }
            val count = it.update(TABLE_NAME, values, CAT_ID + " LIKE ?", arrayOf(cat.id.toString()))
        }
        getCats()
    }

    override fun deleteCat(cat: Cat) {
        writableDatabase.use {
            val deleteRows = it.delete(TABLE_NAME, CAT_ID + " LIKE ?", arrayOf(cat.id.toString()))
        }
        getCats()
    }
}
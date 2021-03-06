package rs.android.task4.repository.databaseCursor

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import rs.android.task4.data.Cat
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
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($CAT_ID VARCHAR(50) PRIMARY KEY, $CAT_NAME VARCHAR(50), $CAT_BIRTHDAY INT, $CAT_BREED VARCHAR(50))"

class CatSQLiteOpenHelper(context: Context, databaseName: String): RepositoryDAO, SQLiteOpenHelper(
    context,
    databaseName,
    null,
    DATABASE_VERSION) {

    private val listCatsLiveData = MutableLiveData<List<Cat>>()
    private val pref = PreferenceManager.getDefaultSharedPreferences(context)

    private fun getFilter(): String{
        return pref.getString("filter_field", "id").toString()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SQL)
        val cat = Cat()
        db.execSQL("INSERT INTO $TABLE_NAME ($CAT_ID, $CAT_NAME, $CAT_BIRTHDAY, $CAT_BREED) VALUES (\"${cat.id.toString()}\", \"${cat.name}\", \"${cat.birthday}\", \"${cat.breed}\");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private fun getCursorWithCats(filter: String): Cursor{
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $filter", null)
    }

    override fun getCats(filter: String): LiveData<List<Cat>> {
        val listOfCats = mutableListOf<Cat>()
        getCursorWithCats(filter).use { cursor ->
            if(cursor.moveToFirst()){
                do {
                    val id = UUID.fromString(cursor.getString(cursor.getColumnIndex(CAT_ID)))
                    val name = cursor.getString(cursor.getColumnIndex(CAT_NAME))
                    val birthday = cursor.getInt(cursor.getColumnIndex(CAT_BIRTHDAY))
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
                put(CAT_BIRTHDAY, cat.birthday)
                put(CAT_BREED, cat.breed)
            }
            it?.insert(TABLE_NAME, null, values)
        }
        getCats(getFilter())
    }

    override fun updateCat(cat: Cat) {
        writableDatabase.use {
            val values = ContentValues().apply {
                put(CAT_NAME, cat.name)
                put(CAT_BIRTHDAY, cat.birthday)
                put(CAT_BREED, cat.breed)
            }
            it.update(TABLE_NAME, values, CAT_ID + " LIKE ?", arrayOf(cat.id.toString()))
        }
        getCats(getFilter())
    }

    override fun deleteCat(cat: Cat) {
        writableDatabase.use {
            it.delete(TABLE_NAME, CAT_ID + " LIKE ?", arrayOf(cat.id.toString()))
        }
        getCats(getFilter())
    }
}
package rs.android.task4

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import rs.android.task4.data.Cat
import rs.android.task4.repository.Repository

class CatFragmentViewModel : ViewModel() {

    private val repository = Repository.get()

    val catsListLiveData: LiveData<List<Cat>> = repository.getCats()

    fun addCat(cat: Cat) {
        repository.addCat(cat)
    }

    fun deleteCat(cat: Cat) {
        repository.deleteCat(cat)
    }
}
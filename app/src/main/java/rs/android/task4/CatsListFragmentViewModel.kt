package rs.android.task4

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import rs.android.task4.data.Cat
import rs.android.task4.repository.Repository

class CatsListFragmentViewModel : ViewModel() {

    private val repository = Repository.get()

    val catsListLiveData: LiveData<List<Cat>> = repository.getCats()

    fun deleteCat(cat: Cat) {
        repository.deleteCat(cat)
    }

    fun updateCatsList(){
        repository.getCats()
    }

}
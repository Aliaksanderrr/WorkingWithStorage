package rs.android.task4

import android.util.Log
import androidx.lifecycle.ViewModel
import rs.android.task4.data.Cat
import rs.android.task4.repository.Repository

class CatInfoFragmentViewModel : ViewModel()  {
    private val repository = Repository.get()

    lateinit var cat: Cat

    fun addCat(cat: Cat){
        repository.addCat(cat)
    }

    fun updateCat(cat: Cat) {
        repository.updateCat(cat)
    }

}
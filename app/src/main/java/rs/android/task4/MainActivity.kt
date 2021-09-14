package rs.android.task4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import rs.android.task4.data.Cat

class MainActivity : AppCompatActivity(), CatsListFragment.CatInfoListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm: FragmentManager = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.main_fragment_container)

        if (currentFragment == null){
            val catListFragment = CatsListFragment.newInstance()
            fm.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_fragment_container, catListFragment)
                .commit()
        }
    }

    override fun chosenCat(cat: Cat) {
        val fm: FragmentManager = supportFragmentManager
        val catFragment = CatInfoFragment.newInstance(cat)
        fm.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.main_fragment_container, catFragment)
            .addToBackStack(null)
            .commit()
    }
}

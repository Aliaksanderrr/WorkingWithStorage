package rs.android.task4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm: FragmentManager = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.main_fragment_container)

        if (currentFragment == null){
            val catFragment = CatFragment.newInstance()
            fm.beginTransaction()
                .add(R.id.main_fragment_container, catFragment)
                .commit()
        }
    }
}

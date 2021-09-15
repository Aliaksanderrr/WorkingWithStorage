package rs.android.task4.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(android.R.id.content, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.title = "Settings"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
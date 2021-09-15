package rs.android.task4.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import rs.android.task4.R

class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        return
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setings)
    }


}
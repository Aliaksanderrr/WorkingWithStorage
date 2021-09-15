package rs.android.task4

import android.app.Application
import rs.android.task4.repository.Repository

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
    }
}
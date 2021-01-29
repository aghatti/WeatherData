package ru.adsc.weatherdata

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.adsc.weatherdata.data.DataRepository
import ru.adsc.weatherdata.data.mRoomDatabase

class mApp: Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { mRoomDatabase.getDatabase(this, applicationScope) }
    //val repository by lazy { DataRepository(database.cityDao()) }
    val repository by lazy { DataRepository(database.cityDao()) }
    override fun onCreate() {
        super.onCreate()
        instance = this
        
    }

    companion object {
        lateinit var instance: mApp
            private set
    }

}
package ru.adsc.weatherdata.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.adsc.weatherdata.data.entities.City

@Database(entities = arrayOf(City::class), version = 4, exportSchema = false)
abstract class mRoomDatabase: RoomDatabase() {
    abstract fun cityDao(): CityDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: mRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): mRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    mRoomDatabase::class.java,
                    "city_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(mDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class mDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.cityDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(cityDao: CityDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            cityDao.deleteAll()

            //var city = City(0, "Hello3")
            var city = City("Moscow", countryCode = "RU")
            cityDao.insert(city)
            //city = City(0,"World!")
            city = City("Saint Petersburg", countryCode = "RU")
            cityDao.insert(city)
        }
    }

}
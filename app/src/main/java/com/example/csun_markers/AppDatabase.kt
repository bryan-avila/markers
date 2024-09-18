package com.example.csun_markers

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Spot::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun spotDAO(): SpotDAO

    private class AppDatabaseCallBack(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        // ----- Initialize the Database upon first ever launch of the app -----
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
                INSTANCE?.let { database ->

                    scope.launch { val spotDAO = database.spotDAO()

                        spotDAO.deleteAll() // Clear the DB to be safe.

                        val spot = Spot("CSUN Library", "Print, Study, Relax!", R.drawable.library)
                        spotDAO.insert(spot) // Add 'Sample' Spot during first ever launch!

                    }
                }
            }
        }

        // This companion object ensures that if there is no instance of the database, then it can be created as "app_database".
        companion object {
            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                            .addCallback(AppDatabaseCallBack(scope))
                        .build()
                    INSTANCE = instance

                    instance // Return the instance of this database!
                }
            }
        }
    }

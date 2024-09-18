package com.example.csun_markers

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

// Create the DB and Repo as a part of the Application, to avoid having
// to re-construct them throughout the app's lifecycle.
class SpotApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val spotDatabase by lazy {
        AppDatabase.getDatabase(this, applicationScope)
    }

    val spotRepo by lazy {
        AppRepo(spotDatabase.spotDAO())
    }
}
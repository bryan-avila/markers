package com.example.csun_markers

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AppRepo(private val spotDao: SpotDAO) {

    // The Flow<List<Spot>> is public property, accessible to ViewModels!
    // Utilize SpotDAO's SQL query here, returning all spots, sorted alphabetically by title.
    val allSpots: Flow<List<Spot>> = spotDao.getSpotsByTitle()
    val allSpotsDESC: Flow<List<Spot>> = spotDao.getSpotsByTitleDESC()

    // This function will be exposed to SpotViewModel!
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(spot: Spot) {
        spotDao.insert(spot)
    }

    // This function will be exposed to SpotViewModel!
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(spot: Spot) {
        spotDao.delete(spot)
    }



}
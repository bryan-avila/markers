package com.example.csun_markers

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotDAO {

    // Return a Flow, which produces values one at a time! Display Spots alphabetically by title.
    @Query("SELECT * FROM spot_table ORDER BY title ASC")
    fun getSpotsByTitle(): Flow<List<Spot>>

    @Query("SELECT * FROM spot_table ORDER BY title DESC")
    fun getSpotsByTitleDESC(): Flow<List<Spot>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(spot: Spot)

    @Delete
    suspend fun delete(spot: Spot)

    @Query("DELETE FROM spot_table")
    suspend fun deleteAll()
}
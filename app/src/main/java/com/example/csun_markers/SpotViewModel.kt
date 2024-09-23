package com.example.csun_markers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SpotViewModel(private val spotRepo: AppRepo): ViewModel() {

    var sortType = SortType.ASC

    // Transform Flow data into LiveData!
    val allSpots: LiveData<List<Spot>> = spotRepo.allSpots.asLiveData()
    val allSpotsDESC: LiveData<List<Spot>> = spotRepo.allSpotsDESC.asLiveData()

    // Encapsulate repository functions insert & delete.
    fun insert(spot: Spot) = viewModelScope.launch {
        spotRepo.insert(spot)
    }

    fun delete(spot: Spot) = viewModelScope.launch {
        spotRepo.delete(spot)
    }

}

class SpotViewModelFactory(private val spotRepo: AppRepo): ViewModelProvider.Factory {

    // ----- Make sure that the ViewModel created is of type SpotViewModel -----
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SpotViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpotViewModel(spotRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel given!")
    }
}
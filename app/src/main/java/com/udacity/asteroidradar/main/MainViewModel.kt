package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateFormatter
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.model.source.AsteroidRepository
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val asteroidRepository: AsteroidRepository) : ViewModel() {

    private var _pictureOfDay: LiveData<PictureOfDay> = MutableLiveData()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private var _asteroids: LiveData<List<Asteroid>> = MutableLiveData()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private var _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid>
        get() = _navigateToDetails

    private val dateFormatter = DateFormatter()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            val todayDate = dateFormatter.parseDate(Date(), Constants.API_QUERY_DATE_FORMAT)

            todayDate?.let {
                _asteroids = asteroidRepository.getAllAsteroidFromDate(it)
            }

            _pictureOfDay = asteroidRepository.getPictureOfTheDay()

        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun onNavigationDone() {
        _navigateToDetails.value = null
    }
}
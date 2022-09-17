package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateFormatter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.model.source.AsteroidRepository
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel(private val asteroidRepository: AsteroidRepository) : ViewModel() {

    private var _pictureOfDay: LiveData<PictureOfDay> = MutableLiveData()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private var _asteroids: MutableLiveData<List<Asteroid>> = MutableLiveData()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private var _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid>
        get() = _navigateToDetails

    private val dateFormatter = DateFormatter()
    private var startDate: String = ""
    private var endDate: String = ""

    init {
        startDate = dateFormatter.parseDate(Date()) ?: ""
        endDate = dateFormatter.parseDate(getDateWeekAfterToday()) ?: ""
        fetchAsteroidsData()
        fetchPictureOfTheDayData()
    }

    private fun fetchPictureOfTheDayData() {
        viewModelScope.launch {
            _pictureOfDay = asteroidRepository.getPictureOfTheDay()
        }
    }

    private fun fetchAsteroidsData() {
        viewModelScope.launch {
            _asteroids.value = asteroidRepository.getAllAsteroidFromDate(startDate, endDate)
        }
    }

    fun onMenuItemSelected(itemId: Int) {
        when (itemId) {
            R.id.show_today_asteroids_menu -> {
                startDate = dateFormatter.parseDate(Date()) ?: ""
                endDate = dateFormatter.parseDate(getTomorrowDate()) ?: ""
                fetchAsteroidsData()
            }
            R.id.show_week_asteroids_menu -> {
                startDate = dateFormatter.parseDate(Date()) ?: ""
                endDate = dateFormatter.parseDate(getDateWeekAfterToday()) ?: ""
                fetchAsteroidsData()
            }
            R.id.show_all_asteroids_menu -> {
                startDate = ""
                endDate = ""
                fetchAsteroidsData()
            }
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun onNavigationDone() {
        _navigateToDetails.value = null
    }

    private fun getTomorrowDate() : Date {
        return Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1))
    }

    private fun getDateWeekAfterToday() : Date {
        return Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7))
    }
}
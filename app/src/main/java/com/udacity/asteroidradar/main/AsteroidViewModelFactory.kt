package com.udacity.asteroidradar.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.model.source.AsteroidRepository

class AsteroidViewModelFactory(private val asteroidRepository: AsteroidRepository) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(asteroidRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
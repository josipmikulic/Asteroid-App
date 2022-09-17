package com.udacity.asteroidradar.model.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.model.source.local.AsteroidLocalDataSource
import com.udacity.asteroidradar.model.source.remote.AsteroidRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class AsteroidRepository(
    private val localDataSource: AsteroidLocalDataSource,
    private val remoteDataSource: AsteroidRemoteDataSource
) {

    suspend fun get(asteroidId: Long): LiveData<Asteroid?> = liveData {
        var asteroid = localDataSource.get(asteroidId)
        emit(asteroid)

        try {
            remoteDataSource.get(asteroidId).let {
                localDataSource.insert(it)
                asteroid = it
            }
        } catch (e: Exception) {
            Log.d("AsteroidRepository", e.stackTraceToString())
        }

        emit(asteroid)
    }

    suspend fun getAllAsteroidFromDate(
        startDate: String,
        endDate: String
    ): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            val asteroids = if (startDate.isEmpty() && endDate.isEmpty()) {
                localDataSource.getAll()
            } else {
                localDataSource.getAllAsteroidsFromDate(startDate, endDate)
            }

            try {
                remoteDataSource.getAllFromDate(startDate, endDate).let { remoteAsteroids ->
                    localDataSource.insertAll(remoteAsteroids)
                }
            } catch (e: Exception) {
                Log.d("AsteroidRepository", e.stackTraceToString())
            }

            asteroids
        }
    }

    suspend fun getPictureOfTheDay(): LiveData<PictureOfDay> = liveData(Dispatchers.IO) {
        try {
            val pictureOfDay = remoteDataSource.getPictureOfTheDay()
            emit(pictureOfDay)
        } catch (e: Exception) {
            Log.d("AsteroidRepository", e.stackTraceToString())
        }
    }

    suspend fun refreshAsteroidData(startDate: String) {
        remoteDataSource.getAllFromDate(startDate, "").let { remoteAsteroids ->
            localDataSource.insertAll(remoteAsteroids)
        }
    }

    suspend fun clearOldAsteroids(date: String) {
        localDataSource.clearOldAsteroids(date)
    }

}
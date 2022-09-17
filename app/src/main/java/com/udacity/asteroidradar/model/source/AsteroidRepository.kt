package com.udacity.asteroidradar.model.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.model.source.local.AsteroidLocalDataSource
import com.udacity.asteroidradar.model.source.remote.AsteroidRemoteDataSource
import kotlinx.coroutines.Dispatchers

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

    suspend fun getAllAsteroidFromDate(date: String): LiveData<List<Asteroid>> =
        liveData(Dispatchers.IO) {
            var asteroids = localDataSource.getAllAsteroidsFromDate(date)
            emit(asteroids)

            try {
                remoteDataSource.getAllFromDate(date).let { remoteAsteroids ->
                    asteroids = remoteAsteroids
                    localDataSource.insertAll(remoteAsteroids)
                }
            } catch (e: Exception) {
                Log.d("AsteroidRepository", e.stackTraceToString())
            }

            emit(asteroids)
        }


    suspend fun getPictureOfTheDay(): LiveData<PictureOfDay> = liveData(Dispatchers.IO) {
        try {
            val pictureOfDay = remoteDataSource.getPictureOfTheDay()
            emit(pictureOfDay)
        } catch (e: Exception) {
            Log.d("AsteroidRepository", e.stackTraceToString())
        }
    }

    suspend fun refreshAsteroidData(date: String) {
        remoteDataSource.getAllFromDate(date).let { remoteAsteroids ->
            localDataSource.insertAll(remoteAsteroids)
        }
    }

    suspend fun clearOldAsteroids(date: String) {
        localDataSource.clearOldAsteroids(date)
    }

}
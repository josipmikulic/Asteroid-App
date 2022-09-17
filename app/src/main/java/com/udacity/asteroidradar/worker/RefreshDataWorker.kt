package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateFormatter
import com.udacity.asteroidradar.model.source.AsteroidRepository
import com.udacity.asteroidradar.model.source.local.AsteroidDatabase
import com.udacity.asteroidradar.model.source.local.AsteroidLocalDataSource
import com.udacity.asteroidradar.model.source.remote.AsteroidRemoteDataSource
import com.udacity.asteroidradar.model.source.remote.AsteroidService
import java.util.*

class RefreshDataWorker(private var appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val dateFormatter = DateFormatter()

    companion object {
        const val WORK_NAME = "com.udacity.asteroidradar.worker.RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val localDataSource =
            AsteroidLocalDataSource(AsteroidDatabase.getInstance(appContext).asteriodDao)
        val remoteDataSource = AsteroidRemoteDataSource(AsteroidService())
        val repository = AsteroidRepository(localDataSource, remoteDataSource)

        return try {
            val todayDate = dateFormatter.parseDate(Date(), Constants.API_QUERY_DATE_FORMAT)

            todayDate?.let {
                repository.clearOldAsteroids(todayDate)
                repository.refreshAsteroidData(todayDate)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
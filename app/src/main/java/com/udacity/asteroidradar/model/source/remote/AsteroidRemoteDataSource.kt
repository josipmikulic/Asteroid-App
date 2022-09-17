package com.udacity.asteroidradar.model.source.remote

import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class AsteroidRemoteDataSource(private val asteroidService: AsteroidService) {

    suspend fun getAllFromDate(startDate: String, endDate: String): List<Asteroid> {
        val jsonObject = asteroidService.retrofitService.getAllFromDate(startDate, endDate)
        return parseAsteroidsJsonResult(JSONObject(jsonObject))
    }

    suspend fun get(asteroidId: Long): Asteroid {
        return asteroidService.retrofitService.get(asteroidId)
    }

    suspend fun getPictureOfTheDay(): PictureOfDay {
        val jsonObject = asteroidService.retrofitService.getPictureOfTheDay()
        return parsePictureOfDayJsonResult(JSONObject(jsonObject))
    }

}

interface AsteroidApi {

    @GET("neo/rest/v1/feed")
    suspend fun getAllFromDate(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): String

    @GET("neo/rest/v1/feed/{asteroidId}")
    suspend fun get(@Path("asteroidId") asteroidId: Long): Asteroid

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(): String

}

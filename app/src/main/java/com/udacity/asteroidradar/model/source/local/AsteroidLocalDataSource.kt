package com.udacity.asteroidradar.model.source.local

import androidx.room.*
import com.udacity.asteroidradar.model.Asteroid

class AsteroidLocalDataSource(private val dataSource: AsteroidDao) {

    suspend fun get(asteroidId: Long): Asteroid? {
        return dataSource.get(asteroidId)
    }

    suspend fun insert(asteroid: Asteroid) {
        dataSource.insert(asteroid)
    }

    suspend fun insertAll(asteroids: List<Asteroid>) {
        asteroids.forEach {
            insert(it)
        }
    }

    suspend fun update(asteroid: Asteroid) {
        dataSource.update(asteroid)
    }

    suspend fun clear() {
        dataSource.clear()
    }

    suspend fun getAll(): List<Asteroid> {
        return dataSource.getAll()
    }


    suspend fun getAllAsteroidsFromDate(date: String): List<Asteroid> {
        return dataSource.getAllAsteroidsFromDate(date)
    }

    suspend fun clearOldAsteroids(date: String) {
        dataSource.clearOldAsteroids(date)
    }

}

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroid: Asteroid)

    @Update
    suspend fun update(asteroid: Asteroid)

    @Query("SELECT * from asteroid_database WHERE id = :asteroidId")
    suspend fun get(asteroidId: Long): Asteroid?

    @Query("DELETE FROM asteroid_database")
    suspend fun clear()

    @Query("SELECT * FROM asteroid_database ORDER BY id ASC")
    fun getAll(): List<Asteroid>

    @Query("SELECT * FROM asteroid_database WHERE closeApproachDate >= :date ORDER BY closeApproachDate ASC")
    fun getAllAsteroidsFromDate(date: String): List<Asteroid>

    @Query("DELETE FROM asteroid_database WHERE closeApproachDate < :date")
    fun clearOldAsteroids(date: String)
}

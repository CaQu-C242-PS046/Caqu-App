package com.budi.caquapplicaton.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CareerRecommendationDao {
    @Query("SELECT * FROM CareerRecommendationEntity ORDER BY id DESC")
    fun getAllRecommendations(): LiveData<List<CareerRecommendationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecommendation(recommendation: CareerRecommendationEntity)

    @Query("DELETE FROM CareerRecommendationEntity")
    suspend fun clearAllRecommendations()
}


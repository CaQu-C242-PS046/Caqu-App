package com.budi.caquapplicaton.room


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CareerRecommendationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recommendation: String
)
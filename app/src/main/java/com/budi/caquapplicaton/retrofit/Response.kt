package com.budi.caquapplicaton.retrofit

import com.google.gson.annotations.SerializedName

data class CareerNameResponse(
	val success: Boolean,
	val history: List<HistoryItem>
)

data class HistoryItem(
	@SerializedName("recommended_career")
	val recommendedCareer: String
)


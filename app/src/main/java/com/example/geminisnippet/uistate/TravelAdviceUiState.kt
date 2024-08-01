package com.example.geminisnippet.uistate

import java.time.LocalDate

data class TravelAdviceUiState(
    val destination: String,
    val interests: List<String>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val itinerary: String? = null,
    val errorMessage: String? = null,
    val loadingStatus: LoadingStatus = LoadingStatus.Idle
) {
    enum class LoadingStatus {
        Idle, Loading, Success, Error
    }
}
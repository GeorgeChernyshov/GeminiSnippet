package com.example.geminisnippet.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminisnippet.repository.TravelAdviceRepository
import com.example.geminisnippet.uistate.TravelAdviceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class TravelAdviceViewModel @Inject constructor(
    private val repository: TravelAdviceRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(TravelAdviceUiState(
        destination = "",
        interests = emptyList(),
        startDate = LocalDate.now(),
        endDate = LocalDate.now(),
        loadingStatus = TravelAdviceUiState.LoadingStatus.Idle
    ))

    val uiState: State<TravelAdviceUiState> = _uiState

    fun updateDestination(destination: String) {
        _uiState.value = _uiState.value.copy(destination = destination)
    }

    fun updateInterests(interests: List<String>) {
        _uiState.value = _uiState.value.copy(interests = interests)
    }

    fun updateStartDate(startDate: LocalDate) {
        _uiState.value = _uiState.value.copy(startDate = startDate)
    }

    fun updateEndDate(endDate: LocalDate) {
        _uiState.value = _uiState.value.copy(endDate = endDate)
    }

    fun generateItinerary() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingStatus = TravelAdviceUiState.LoadingStatus.Loading)
            try {
                val daysBetween = ChronoUnit.DAYS.between(
                    uiState.value.startDate,
                    uiState.value.endDate
                )

                val startDate = uiState.value.startDate.format(DateTimeFormatter.ISO_DATE)

                val prompt = "I'm traveling to ${uiState.value.destination} " +
                        "for ${daysBetween.toInt() + 1} " +
                        "days from ${startDate}. " +
                        "I'm interested in ${uiState.value.interests.joinToString()}. " +
                        "Can you suggest a detailed itinerary?"

                val response = repository.generateItinerary(prompt)
                _uiState.value = _uiState.value.copy(
                    itinerary = response,
                    loadingStatus = TravelAdviceUiState.LoadingStatus.Success
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Unknown error",
                    loadingStatus = TravelAdviceUiState.LoadingStatus.Error
                )
            }
        }
    }
}
package com.example.geminisnippet.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geminisnippet.uistate.TravelAdviceUiState
import com.example.geminisnippet.viewmodel.TravelAdviceViewModel
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun TravelAdviceScreen(viewModel: TravelAdviceViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (uiState.loadingStatus == TravelAdviceUiState.LoadingStatus.Loading) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            TravelAdvice(
                uiState = uiState,
                onDestinationChange = { viewModel.updateDestination(it) },
                onInterestsChange = { viewModel.updateInterests(it) },
                onStartDateChange = { viewModel.updateStartDate(it) },
                onEndDateChange = { viewModel.updateEndDate(it) },
                onGenerateItineraryClick = { viewModel.generateItinerary() }
            )
        }

        when (uiState.loadingStatus) {
            TravelAdviceUiState.LoadingStatus.Success -> uiState.itinerary?.let {
                Text(
                    it,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            TravelAdviceUiState.LoadingStatus.Error -> uiState.errorMessage?.let {
                Text(
                    "Error: $it",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TravelAdvice(
    uiState: TravelAdviceUiState,
    onDestinationChange: (String) -> Unit,
    onInterestsChange: (List<String>) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onGenerateItineraryClick: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        OutlinedTextField(
        value = uiState.destination,
        onValueChange = onDestinationChange,
        label = { Text("Destination") },
        modifier = Modifier.fillMaxWidth()
    )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Interests:")
        FlowRow {
            listOf("History", "Art", "Food", "Nature", "Nightlife").forEach { interest ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = uiState.interests.contains(interest),
                        onCheckedChange = { checked ->
                            val updatedInterests = if (checked) {
                                uiState.interests + interest
                            } else {
                                uiState.interests - interest
                            }
                            onInterestsChange(updatedInterests)
                        }
                    )
                    Text(interest)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val startDateState = rememberDatePickerState()

        LaunchedEffect(startDateState.selectedDateMillis) {
            if (startDateState.selectedDateMillis != null) {
                onStartDateChange(LocalDate.ofEpochDay(startDateState.selectedDateMillis!! / 86400000))
            }
        }

        DatePicker(
            state = startDateState,
            title = { Text("Start Date") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        val endDateState = rememberDatePickerState()

        LaunchedEffect(endDateState.selectedDateMillis) {
            if (endDateState.selectedDateMillis != null) {
                onEndDateChange(LocalDate.ofEpochDay(endDateState.selectedDateMillis!! / 86400000))
            }
        }

        DatePicker(
            state = endDateState,
            title = { Text("End Date") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGenerateItineraryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Itinerary")
        }
    }
}
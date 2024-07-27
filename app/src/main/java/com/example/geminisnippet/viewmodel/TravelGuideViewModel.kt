package com.example.geminisnippet.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.geminisnippet.uistate.TravelGuideUiState

class TravelGuideViewModel : ViewModel() {
    private val _uiState = mutableStateOf(TravelGuideUiState())
    val uiState: State<TravelGuideUiState> = _uiState

    // Function to update imageUri
    fun updateImageUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun updateAnalysisResult(result: String?) {
        _uiState.value = _uiState.value.copy(analysisResult = result)
    }

    fun updateShowDialog(showDialog: Boolean) {
        _uiState.value = _uiState.value.copy(showDialog = showDialog)
    }
}
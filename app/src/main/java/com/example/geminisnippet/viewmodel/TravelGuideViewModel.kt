package com.example.geminisnippet.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminisnippet.repository.TravelGuideRepository
import com.example.geminisnippet.uistate.TravelGuideUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelGuideViewModel @Inject constructor(
    private val repository: TravelGuideRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(TravelGuideUiState())
    val uiState: State<TravelGuideUiState> = _uiState

    // Function to update imageUri
    fun updateImageUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
        if (uri != null) {
            analyzeImage(uri)
        }
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

    private fun analyzeImage(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = repository.analyzeImage(imageUri)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                analysisResult = if (result.isSuccess) {
                    result.getOrNull()
                } else {
                    "Analysis failed: ${result.exceptionOrNull()?.message}"
                }
            )
        }
    }
}
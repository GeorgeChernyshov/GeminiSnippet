package com.example.geminisnippet.uistate

import android.net.Uri

data class TravelGuideUiState(
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val analysisResult: String? = null,
    val showDialog: Boolean = false
)
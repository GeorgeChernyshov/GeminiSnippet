package com.example.geminisnippet.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class TravelAdviceRepository @Inject constructor(
    private val generativeModel: GenerativeModel
) {
    suspend fun generateItinerary(prompt: String): String {
        val response = generativeModel.generateContent(
            content { text(prompt) }
        )

        return response.text ?: ""
    }
}
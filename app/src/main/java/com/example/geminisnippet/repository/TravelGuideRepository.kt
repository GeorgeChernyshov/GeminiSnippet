package com.example.geminisnippet.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.geminisnippet.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TravelGuideRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageLoader: ImageLoader
) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash-001",
        apiKey = BuildConfig.apiKey
    )

    suspend fun analyzeImage(imageUri: Uri): Result<String> {
        val request = ImageRequest.Builder(context)
            .data(imageUri)
            .build()

        return try {
            val bitmap = (imageLoader.execute(request) as SuccessResult)
                .drawable
                .toBitmap()
            val result = sendPrompt(bitmap, PROMPT)

            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun sendPrompt(
        bitmap: Bitmap,
        prompt: String
    ): Result<String> {
        return try {
            val response = generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(prompt)
                }
            )
            response.text?.let { outputContent ->
                Result.success(outputContent)
            } ?: Result.failure(PromptNotFoundException(EXCEPTION_MESSAGE))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    class PromptNotFoundException(message: String) : Exception(message)

    companion object {
        private const val EXCEPTION_MESSAGE = "Gemini couldn't find the prompt for the chosen image"
        private const val PROMPT = "Identify the landmark in this image and provide a concise summary including historical significance, key features, and interesting facts."
    }
}
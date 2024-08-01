package com.example.geminisnippet.di

import com.example.geminisnippet.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Provides
    fun provideGenerativeModel() = GenerativeModel(
        modelName = "gemini-1.5-flash-001",
        apiKey = BuildConfig.apiKey
    )
}
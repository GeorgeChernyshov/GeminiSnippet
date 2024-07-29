package com.example.geminisnippet.di

import android.content.Context
import coil.ImageLoader
import com.example.geminisnippet.repository.TravelGuideRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object TravelGuideModule {

    @Provides
    fun provideTravelGuideRepository(
        @ApplicationContext context: Context,
        imageLoader: ImageLoader
    ): TravelGuideRepository {
        return TravelGuideRepository(
            context, imageLoader
        )
    }

    @Provides
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader(context)
    }
}
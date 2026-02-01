package com.shalenmathew.quotesapp.presentation.widget

import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.LikedQuote
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun likedQuote(): LikedQuote
    fun updateWidgetUseCase(): UpdateWidgetUseCase
}
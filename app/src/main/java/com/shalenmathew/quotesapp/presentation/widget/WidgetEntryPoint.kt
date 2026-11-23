package com.shalenmathew.quotesapp.presentation.widget

import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.LikedQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.library.CollectionUseCases
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetUseCase
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun likedQuote(): LikedQuote
    fun updateWidgetUseCase(): UpdateWidgetUseCase
    fun quoteUseCase(): QuoteUseCase
    fun customQuoteUseCases(): CustomQuoteUseCases
    fun collectionUseCases(): CollectionUseCases
    fun scheduleWidgetRefresh(): ScheduleWidgetRefresh
}
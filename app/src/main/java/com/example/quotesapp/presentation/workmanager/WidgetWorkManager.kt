package com.example.quotesapp.presentation.workmanager

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.example.quotesapp.presentation.widget.QuotesWidgetObj
import com.example.quotesapp.util.QUOTE_KEY
import com.example.quotesapp.util.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class WidgetWorkManager(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var quoteUseCase: QuoteUseCase

    override suspend fun doWork(): Result {

        val glanceAppWidgetManager = GlanceAppWidgetManager(applicationContext)
        val widgetIds = glanceAppWidgetManager.getGlanceIds(QuotesWidgetObj::class.java)

//        val savedQuote = runBlocking {
//            applicationContext.dataStore.data.first()[QUOTE_KEY] ?: "No quote saved yet"
//        }

        widgetIds.forEach { it->
            QuotesWidgetObj.updateAll(applicationContext)
        }

        return Result.success()

    }

}
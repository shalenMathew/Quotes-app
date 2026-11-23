package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.library.CollectionUseCases
import com.shalenmathew.quotesapp.util.Constants
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_WIDGET_REFRESH_INTERVAL_MINUTES
import com.shalenmathew.quotesapp.util.Resource
import com.shalenmathew.quotesapp.util.getMillisFromNowMinutes
import com.shalenmathew.quotesapp.util.getWidgetRefreshIntervalMinutes
import com.shalenmathew.quotesapp.util.getWidgetSources
import com.shalenmathew.quotesapp.util.setLastAlarmTriggerMillis
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

class RefreshWidgetActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val currentQuoteId = parameters[WidgetKeys.quoteIdKey] ?: -1

        runCatching {
            val entryPoint = EntryPointAccessors.fromApplication(
                context,
                WidgetEntryPoint::class.java
            )
            val quoteUseCase = entryPoint.quoteUseCase()
            val customQuoteUseCases = entryPoint.customQuoteUseCases()
            val collectionUseCases = entryPoint.collectionUseCases()

            val nextQuote = pickNextQuoteFromSources(
                context = context,
                quoteUseCase = quoteUseCase,
                customQuoteUseCases = customQuoteUseCases,
                collectionUseCases = collectionUseCases,
                currentQuoteId = currentQuoteId
            )

            if (nextQuote != null) {
                entryPoint.updateWidgetUseCase()(nextQuote)
                    .onFailure { Log.w(TAG, "Widget manual refresh failed: ${it.message}") }

                val intervalMinutes = context.getWidgetRefreshIntervalMinutes().first()
                    ?: DEFAULT_WIDGET_REFRESH_INTERVAL_MINUTES
                val nextTriggerMillis = getMillisFromNowMinutes(intervalMinutes)
                entryPoint.scheduleWidgetRefresh().scheduleWidgetRefreshWorkAlarm(nextTriggerMillis)
                context.setLastAlarmTriggerMillis(System.currentTimeMillis())
            } else {
                // Fallback to work manager refresh
                entryPoint.scheduleWidgetRefresh().scheduleWidgetRefreshWorkManager()
            }
        }.onFailure {
            Log.e(TAG, "Failed to manually refresh widget: ${it.message}", it)
        }
    }

    private suspend fun pickNextQuoteFromSources(
        context: Context,
        quoteUseCase: QuoteUseCase,
        customQuoteUseCases: CustomQuoteUseCases,
        collectionUseCases: CollectionUseCases,
        currentQuoteId: Int
    ): Quote? {
        val enabledSources = context.getWidgetSources().first().toList()
        if (enabledSources.isEmpty()) return null

        val candidatePool = mutableListOf<Quote>()

        if ("favorites" in enabledSources) {
            runCatching {
                quoteUseCase.getLikedQuotes().first()
            }.getOrNull()?.let { candidatePool.addAll(it) }
        }

        if ("custom" in enabledSources) {
            runCatching {
                customQuoteUseCases.getCustomQuotes("").first().map { customQuote ->
                    Quote(
                        id = customQuote.id,
                        quote = customQuote.quote,
                        author = customQuote.author,
                        liked = false,
                        isCustom = true
                    )
                }
            }.getOrNull()?.let { candidatePool.addAll(it) }
        }

        enabledSources.forEach { source ->
            val collectionId = Constants.parseCollectionIdFromSource(source)
            if (collectionId != null) {
                runCatching {
                    collectionUseCases.searchQuotesInCollection(collectionId, "").first()
                }.getOrNull()?.let { candidatePool.addAll(it) }
            }
        }

        val uniquePool = candidatePool.distinctBy { "${it.isCustom}_${it.id}_${it.quote}" }

        // If network is enabled and either chosen or local pool is empty/single
        val nonCurrentLocal = uniquePool.filter { it.id != currentQuoteId }
        if ("network" in enabledSources && (nonCurrentLocal.isEmpty() || enabledSources.random() == "network")) {
            val remoteQuote = runCatching {
                when (val response = withTimeoutOrNull(NETWORK_TIMEOUT_MILLIS) {
                    quoteUseCase.getRandomRemoteQuote()
                }) {
                    is Resource.Success -> response.data
                    else -> null
                }
            }.getOrNull()
            if (remoteQuote != null && (remoteQuote.id != currentQuoteId || uniquePool.isEmpty())) {
                return remoteQuote
            }
        }

        if (nonCurrentLocal.isNotEmpty()) {
            return nonCurrentLocal.random()
        }

        return uniquePool.firstOrNull() ?: quoteUseCase.getLatestQuote()
    }

    companion object {
        private const val TAG = "RefreshWidgetAction"
        private const val NETWORK_TIMEOUT_MILLIS = 6_000L
    }
}

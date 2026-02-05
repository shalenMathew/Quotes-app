package com.shalenmathew.quotesapp.data.repository

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.GlanceWidgetManager
import com.shalenmathew.quotesapp.domain.repository.WidgetRepository
import com.shalenmathew.quotesapp.presentation.widget.QuotesWidgetObj
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_ID_KEY
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_KEY
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_LIKED_KEY
import javax.inject.Inject

class WidgetRepositoryImpl @Inject constructor(
    private val glanceWidgetManager: GlanceWidgetManager
) : WidgetRepository {

    override suspend fun updateWidget(quote: Quote): Result<Unit> =
        withValidQuoteId(quote) { quoteId ->
            updateWidgets { prefs ->
                prefs.toMutablePreferences().apply {
                    setQuoteData(quote, quoteId)
                }
            }
        }

    override suspend fun updateWidgetIfSameOrEmpty(quote: Quote): Result<Unit> =
        withValidQuoteId(quote) { quoteId ->
            updateWidgets { prefs ->
                val currentQuoteId = prefs[WIDGET_QUOTE_ID_KEY]
                if (currentQuoteId == null || currentQuoteId == quoteId) {
                    prefs.toMutablePreferences().apply { setQuoteData(quote, quoteId) }
                } else {
                    prefs
                }
            }
        }

    private suspend fun updateWidgets(
        updateState: (Preferences) -> Preferences
    ): Result<Unit> = runCatching {
        val widgetIds = glanceWidgetManager.getWidgetIds(QuotesWidgetObj::class)
        widgetIds.forEach { glanceId ->
            glanceWidgetManager.updateWidgetState(glanceId, updateState)
        }
        glanceWidgetManager.updateAllWidgets(QuotesWidgetObj)
    }

    private inline fun <T> withValidQuoteId(
        quote: Quote,
        block: (Int) -> Result<T>
    ): Result<T> = quote.id?.let(block)
        ?: Result.failure(IllegalArgumentException("Quote id is null"))

    private fun MutablePreferences.setQuoteData(quote: Quote, quoteId: Int) {
        this[WIDGET_QUOTE_KEY] = quote.quote
        this[WIDGET_QUOTE_ID_KEY] = quoteId
        this[WIDGET_QUOTE_LIKED_KEY] = quote.liked
    }
}
package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.shalenmathew.quotesapp.presentation.MainActivity
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_ID_KEY
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_KEY
import com.shalenmathew.quotesapp.util.WIDGET_THEME_ID_KEY
import com.shalenmathew.quotesapp.util.dataStore
import kotlinx.coroutines.flow.first

object QuotesWidgetObj : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val deprecatedQuote =
            context.dataStore.data.first()[WIDGET_QUOTE_KEY]

        val defaultMessage =
            "Widget is refreshing, will be updated in some time. Or try rebooting the device"
        provideContent {
            val prefs = currentState<Preferences>()
            val savedQuote = prefs[WIDGET_QUOTE_KEY] ?: deprecatedQuote ?: defaultMessage
            val quoteId = prefs[WIDGET_QUOTE_ID_KEY] ?: -1
            val themeId = prefs[WIDGET_THEME_ID_KEY] ?: "default"

            QuoteWidget(
                savedQuote = savedQuote,
                quoteId = quoteId,
                themeId = themeId
            )
        }
    }
}

@Composable
fun QuoteWidget(savedQuote: String, quoteId: Int, themeId: String) {
    val theme = WidgetThemeRegistry.getTheme(themeId)

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable(actionStartActivity<MainActivity>())
    ) {
        theme.contentLayout(savedQuote, quoteId, theme)
    }
}

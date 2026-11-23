package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.MainActivity
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_AUTHOR_KEY
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
        val dsPrefs = context.dataStore.data.first()
        val deprecatedQuote = dsPrefs[WIDGET_QUOTE_KEY]
        val deprecatedAuthor = dsPrefs[WIDGET_QUOTE_AUTHOR_KEY]

        val defaultMessage =
            "Widget is refreshing, will be updated in some time. Or try rebooting the device"
        provideContent {
            val prefs = currentState<Preferences>()
            val rawQuote = prefs[WIDGET_QUOTE_KEY] ?: deprecatedQuote ?: defaultMessage
            val rawAuthor = prefs[WIDGET_QUOTE_AUTHOR_KEY] ?: deprecatedAuthor ?: ""
            val quoteId = prefs[WIDGET_QUOTE_ID_KEY] ?: -1
            val themeId = prefs[WIDGET_THEME_ID_KEY] ?: "default"

            val savedQuote = formatWidgetDisplayQuote(rawQuote, rawAuthor)

            QuoteWidget(
                savedQuote = savedQuote,
                quoteId = quoteId,
                themeId = themeId
            )
        }
    }
}

internal fun formatWidgetDisplayQuote(quote: String, author: String): String {
    val cleanQuote = quote.trim()
    val cleanAuthor = author.trim()
    if (cleanAuthor.isEmpty() ||
        cleanAuthor.equals("Unknown", ignoreCase = true) ||
        cleanQuote.contains(cleanAuthor)
    ) {
        return cleanQuote
    }
    val hasCjk = cleanAuthor.any {
        Character.UnicodeScript.of(it.code) == Character.UnicodeScript.HAN
    }
    return if (hasCjk && !cleanQuote.contains('\n')) {
        "$cleanQuote\n$cleanAuthor"
    } else {
        "$cleanQuote\n— $cleanAuthor"
    }
}

@Composable
fun QuoteWidget(savedQuote: String, quoteId: Int, themeId: String) {
    val theme = WidgetThemeRegistry.getTheme(themeId)

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable(actionStartActivity<MainActivity>()),
        contentAlignment = Alignment.BottomEnd
    ) {
        theme.contentLayout(savedQuote, quoteId, theme)

        Box(
            modifier = GlanceModifier
                .padding(end = 6.dp, bottom = 4.dp)
                .size(44.dp)
                .clickable(
                    actionRunCallback<RefreshWidgetActionCallback>(
                        actionParametersOf(WidgetKeys.quoteIdKey to quoteId)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_widget_refresh),
                contentDescription = "Refresh quote",
                colorFilter = ColorFilter.tint(
                    ColorProvider(theme.quoteTextColor, theme.quoteTextColor)
                ),
                modifier = GlanceModifier.size(28.dp)
            )
        }
    }
}

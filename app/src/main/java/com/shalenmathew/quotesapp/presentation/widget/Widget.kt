package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.MainActivity
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_ID_KEY
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_KEY
import com.shalenmathew.quotesapp.util.WIDGET_THEME_ID_KEY
import com.shalenmathew.quotesapp.util.dataStore
import kotlinx.coroutines.flow.first


import androidx.glance.text.TextAlign

object QuotesWidgetObj : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        // Fallback for widgets that haven't updated to Glance state yet
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
    val radius = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        android.R.dimen.system_app_widget_background_radius
    } else {
        null
    }

    val backgroundModifier = if (theme.backgroundResource != null) {
        GlanceModifier.background(ImageProvider(theme.backgroundResource))
    } else if (theme.backgroundColor != null) {
        GlanceModifier.background(theme.backgroundColor)
    } else {
        GlanceModifier.background(Color.Black)
    }

    val isMinimalTheme = themeId == "all_black" || themeId == "all_white"

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .then(
                if (radius != null) GlanceModifier.cornerRadius(radius)
                else GlanceModifier.cornerRadius(16.dp)
            )
            .then(backgroundModifier)
            .padding(20.dp)
            .clickable(actionStartActivity<MainActivity>()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = if (isMinimalTheme) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = if (isMinimalTheme) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Column(
                modifier = GlanceModifier.defaultWeight(),
                horizontalAlignment = if (isMinimalTheme) Alignment.CenterHorizontally else Alignment.Start
            ) {
                if (!isMinimalTheme) {
                    Text(
                        text = "A gentle reminder for today",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = ColorProvider(theme.labelTextColor, theme.labelTextColor),
                            fontWeight = FontWeight.Normal,
                        ),
                        modifier = GlanceModifier.padding(bottom = 8.dp)
                    )
                }

                Text(
                    text = savedQuote,
                    style = TextStyle(
                        fontSize = if (isMinimalTheme) 16.sp else 14.sp,
                        color = ColorProvider(theme.quoteTextColor, theme.quoteTextColor),
                        fontWeight = FontWeight.Medium,
                        textAlign = if (isMinimalTheme) TextAlign.Center else TextAlign.Start
                    )
                )
            }

            if (!isMinimalTheme) {
                Spacer(modifier = GlanceModifier.width(15.dp))

                val prismImages = theme.prismImages
                val selectedPrism = if (quoteId != -1) prismImages[Math.abs(quoteId) % prismImages.size] else R.drawable.prism3

                Image(
                    provider = ImageProvider(selectedPrism),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = GlanceModifier.size(60.dp)
                )
            }
        }
    }
}


package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
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
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_LIKED_KEY
import com.shalenmathew.quotesapp.util.dataStore
import kotlinx.coroutines.flow.first


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
            val isLiked = prefs[WIDGET_QUOTE_LIKED_KEY] ?: false

            QuoteWidget(
                savedQuote = savedQuote,
                isLiked = isLiked,
                quoteId = quoteId,
            )
        }
    }
}

@Composable
fun QuoteWidget(savedQuote: String, isLiked: Boolean, quoteId: Int) {

    val radius = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        android.R.dimen.system_app_widget_background_radius
    } else {
        null
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .then(
                if (radius != null) GlanceModifier.cornerRadius(radius)
                else GlanceModifier.cornerRadius(16.dp)
            )
            .background(ImageProvider(R.drawable.widget_prism_bg))
//            .background(Color.Black)
            .padding(20.dp)
            .clickable(actionStartActivity<MainActivity>()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = GlanceModifier.defaultWeight()
            ) {
                Text(
                    text = "A gentle reminder for today",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = ColorProvider(Color.White.copy(alpha = 0.6f), Color.White.copy(alpha = 0.6f)),
                        fontWeight = FontWeight.Normal,
                    ),
                    modifier = GlanceModifier.padding(bottom = 8.dp)
                )

                Text(
                    text = savedQuote,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = ColorProvider(Color.White, Color.White),
                        fontWeight = FontWeight.Medium,
                    )
                )
            }

            Spacer(modifier = GlanceModifier.width(15.dp))

            val prismImages = listOf(R.drawable.prism,
                R.drawable.prism2,
                R.drawable.prism3,
                R.drawable.prism4,
                R.drawable.prism5,
                R.drawable.prism6,
                R.drawable.prism7,
                R.drawable.prism8,
                )
            val selectedPrism = if (quoteId != -1) prismImages[Math.abs(quoteId) % prismImages.size] else R.drawable.prism3

            Image(
                provider = ImageProvider(selectedPrism),
                contentDescription = null,
                contentScale = androidx.glance.layout.ContentScale.Crop,
                modifier = GlanceModifier.size(60.dp)
            )
        }

        if (quoteId != -1) {
            Row(
                modifier = GlanceModifier.fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.End
            ) {
                Image(
                    provider = ImageProvider(
                        if (isLiked) R.drawable.heart_filled
                        else R.drawable.heart_unfilled
                    ),
                    contentDescription = "Like",
                    colorFilter = ColorFilter.tint(ColorProvider(Color.White, Color.White)),
                    modifier = GlanceModifier
                        .size(24.dp)
                        .padding(top = 8.dp)
                        .clickable(
                            actionRunCallback<ToggleLikeActionCallback>(
                                actionParametersOf(
                                    WidgetKeys.quoteIdKey to quoteId,
                                )
                            )
                        )
                )
            }
        }
    }
}

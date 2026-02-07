package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
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
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentHeight
import androidx.glance.layout.wrapContentSize
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
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
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .clickable(actionStartActivity<MainActivity>()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            provider = ImageProvider(R.drawable.quotation),
            contentDescription = null,
            modifier = GlanceModifier
                .padding(start = 12.dp, top = 10.dp)
                .size(30.dp)
        )

        Text(
            text = savedQuote,
            style = TextStyle(
                fontSize = 18.sp,
                color = ColorProvider(Color.White),
                fontWeight = FontWeight.Normal,
            ),
            modifier = GlanceModifier.wrapContentSize()
                .padding(horizontal = 15.dp, vertical = 15.dp)
        )

        if (quoteId != -1) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Image(
                    provider = ImageProvider(
                        if (isLiked) R.drawable.heart_filled
                        else R.drawable.heart_unfilled
                    ),
                    contentDescription = "Like",
                    modifier = GlanceModifier
                        .size(28.dp)
                        .padding(end = 8.dp, bottom = 8.dp)
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

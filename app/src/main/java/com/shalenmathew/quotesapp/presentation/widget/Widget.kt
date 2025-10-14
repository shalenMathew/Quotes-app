package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentHeight
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.unit.Dp
import androidx.glance.unit.dp
import androidx.glance.unit.sp
import androidx.compose.runtime.Composable
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.MainActivity
import com.shalenmathew.quotesapp.util.getSavedWidgetQuoteObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object QuotesWidgetObj: GlanceAppWidget() {

    const val ACTION_LIKE_QUOTE = "com.shalenmathew.quotesapp.action.LIKE_QUOTE"

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val savedQuote = runBlocking {
            context.getSavedWidgetQuoteObject().first()
        }

        Log.d("WID,","quote $savedQuote")

        provideContent {
            if (savedQuote != null) {
                QuoteWidget(savedQuote)
            } else {
                QuoteWidgetFallback()
            }
        }

    }
}

@Composable
fun QuoteWidget(savedQuote: Quote) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(androidx.glance.unit.ColorProvider(android.graphics.Color.BLACK))
            .padding(12.dp),
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
            text = savedQuote.quote,
            style = TextStyle(
                fontSize = 18.sp,
                color = ColorProvider(android.graphics.Color.WHITE),
                fontWeight = FontWeight.Normal,
            ),
            modifier = GlanceModifier.wrapContentSize().padding(15.dp)
        )

        // Author
        Text(
            text = "- ${savedQuote.author}",
            style = TextStyle(
                fontSize = 14.sp,
                color = ColorProvider(android.graphics.Color.GRAY),
                fontWeight = FontWeight.Normal,
            ),
            modifier = GlanceModifier.padding(bottom = 10.dp)
        )

        // Like button
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Image(
                provider = ImageProvider(
                    if (savedQuote.liked) R.drawable.heart_filled else R.drawable.heart_unfilled
                ),
                contentDescription = if (savedQuote.liked) "Unlike" else "Like",
                modifier = GlanceModifier
                    .size(24.dp)
                    .clickable(
                        actionStartActivity<MainActivity>()
                    )
            )
        }
    }
}

@Composable
fun QuoteWidgetFallback() {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(androidx.glance.unit.ColorProvider(android.graphics.Color.BLACK))
            .padding(12.dp)
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
            text = "Widget is refreshing, will be updated in some time. Or try rebooting the device",
            style = TextStyle(
                fontSize = 14.sp,
                color = ColorProvider(android.graphics.Color.WHITE),
                fontWeight = FontWeight.Normal,
            ),
            modifier = GlanceModifier.wrapContentSize().padding(15.dp)
        )
    }
}

class QuotesWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = QuotesWidgetObj

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("WorkManagerStatus", "Widget enabled, scheduling update")
    }

}


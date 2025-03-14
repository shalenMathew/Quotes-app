package com.example.quotesapp.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentHeight
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.quotesapp.R
import com.example.quotesapp.util.QUOTE_KEY
import com.example.quotesapp.util.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object CounterWidget: GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val savedQuote = runBlocking {
            context.dataStore.data.first()[QUOTE_KEY] ?: "No quote saved yet"
        }

        provideContent {
            QuoteWidget(savedQuote)
        }

    }
}


@Composable
fun QuoteWidget(savedQuote: String) {
    Column(
        modifier = GlanceModifier.fillMaxWidth().wrapContentHeight()
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 5.dp),
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
            modifier = GlanceModifier.wrapContentSize().padding(horizontal = 15.dp, vertical = 15.dp)
        )
    }
}

class SimpleCounterWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CounterWidget
}





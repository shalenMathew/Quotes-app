package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.MainActivity
import com.shalenmathew.quotesapp.presentation.workmanager.widget.WidgetWorkManager
import com.shalenmathew.quotesapp.util.WIDGET_QUOTE_KEY
import com.shalenmathew.quotesapp.util.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

object QuotesWidgetObj: GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val savedQuote = runBlocking {
            context.dataStore.data.first()[WIDGET_QUOTE_KEY] ?: "widget is refreshing, will be updated in some time.Or try rebooting the device"
        }

        Log.d("WID,","quote $savedQuote")

        provideContent {
            QuoteWidget(context,savedQuote)
        }

    }
}

@Composable
fun QuoteWidget(context: Context, savedQuote: String) {
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
            modifier = GlanceModifier.wrapContentSize().padding(horizontal = 15.dp, vertical = 15.dp)
        )
    }
}

class QuotesWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = QuotesWidgetObj

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("WorkManagerStatus", "Widget enabled, scheduling update")
//        scheduleWidgetUpdate(context)
    }

    private fun scheduleWidgetUpdate(context: Context) {

        val workRequest = PeriodicWorkRequestBuilder<WidgetWorkManager>(15, TimeUnit.HOURS).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "quotes_widget_update",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )


//        val workRequest = OneTimeWorkRequestBuilder<WidgetWorkManager>()
//            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // Runs instantly
//            .build()
//
//        WorkManager.getInstance(context).enqueueUniqueWork(
//            "quotes_widget_update",
//            ExistingWorkPolicy.REPLACE, // Ensures it runs fresh every time
//            workRequest
//        )

    }

}


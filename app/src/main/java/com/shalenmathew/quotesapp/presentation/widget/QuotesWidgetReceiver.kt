package com.shalenmathew.quotesapp.presentation.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.shalenmathew.quotesapp.presentation.workmanager.widget.WidgetWorkManager

class QuotesWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = QuotesWidgetObj

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        enqueueWidgetUpdate(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        enqueueWidgetUpdate(context)
    }

    private fun enqueueWidgetUpdate(context: Context) {
        runCatching {
            val workRequest = OneTimeWorkRequestBuilder<WidgetWorkManager>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                WIDGET_UPDATE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                workRequest
            )
        }.onFailure { exception ->
            Log.e(TAG, "Failed to enqueue widget update", exception)
        }
    }

    companion object {
        private const val TAG = "QuotesWidgetReceiver"
        private const val WIDGET_UPDATE_WORK_NAME = "quotes_widget_update"
    }
}
package com.shalenmathew.quotesapp.presentation.widget

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import dagger.hilt.android.EntryPointAccessors

class ToggleLikeActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val quoteId = parameters[WidgetKeys.quoteIdKey] ?: return

        runCatching {
            val entryPoint = EntryPointAccessors.fromApplication(
                context,
                WidgetEntryPoint::class.java
            )

            val likedQuote = entryPoint.likedQuote()(quoteId) ?: run {
                Log.w(TAG, "Unable to like the quote with id: $quoteId")
                return
            }

            entryPoint.updateWidgetUseCase()(likedQuote)
                .onFailure { Log.w(TAG, "Widget update failed: ${it.message}") }
        }.onFailure {
            Log.e(TAG, "Failed to toggle like for quote $quoteId: ${it.message}")
        }
    }

    companion object {
        private const val TAG = "ToggleLikeActionCallback"
    }
}
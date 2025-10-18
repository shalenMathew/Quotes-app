package com.shalenmathew.quotesapp.presentation.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.shalenmathew.quotesapp.presentation.MainActivity
import com.shalenmathew.quotesapp.util.toggleWidgetQuoteLike
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WidgetActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        when (intent.action) {
            ACTION_TOGGLE_LIKE -> {
                Log.d("WidgetAction", "Toggle like action received")
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        context.toggleWidgetQuoteLike()

                        // Also send intent to MainActivity to handle favorites saving
                        val mainIntent = Intent(context, MainActivity::class.java).apply {
                            action = ACTION_SAVE_TO_FAVORITES
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(mainIntent)
                    } catch (e: Exception) {
                        Log.e("WidgetAction", "Error toggling like", e)
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_TOGGLE_LIKE = "com.shalenmathew.quotesapp.action.TOGGLE_LIKE"
        const val ACTION_SAVE_TO_FAVORITES = "com.shalenmathew.quotesapp.action.SAVE_TO_FAVORITES"
    }
}

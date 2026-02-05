package com.shalenmathew.quotesapp.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.gson.Gson
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.MainActivity
import com.shalenmathew.quotesapp.presentation.receivers.FavActionBroadcastReceiver


fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Quotes Notifications"
        val descriptionText = "Your daily, curated quote to fuel your mind and spirit."
        val importance = NotificationManager.IMPORTANCE_HIGH

        val soundUri = (ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                packageName + "/" + R.raw.flute_notification_tone).toUri()
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val channel =
            NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID_NEW, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
                setSound(soundUri, audioAttributes)
            }

        val notificationWorkManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        try {
            notificationWorkManager.deleteNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID)
        } catch (_: Exception) {
        }

        notificationWorkManager.createNotificationChannel(channel)
    }
}

fun Context.createOrUpdateNotification(quote: Quote) {
//    val notificationLayout = RemoteViews(packageName, R.layout.notification_view)
//    notificationLayout.setTextViewText(R.id.nv_title, quote.quote)
//    notificationLayout.setTextViewText(R.id.nv_author, quote.author)

    val intent = Intent(applicationContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("shortcut_nav", "share")
        putExtra("quote", Gson().toJson(quote))
    }

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notificationCompatBuilder =
        NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID_NEW)
            .clearActions()
            .setSmallIcon(R.drawable.notification_icon_black)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle("")
                    .bigText(quote.quote)
                    .setSummaryText(quote.author)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setContentTitle(quote.quote)
            .setSubText(quote.author)

    notificationCompatBuilder.addAction(applicationContext.getNotificationAction(quote))

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        NotificationManagerCompat.from(applicationContext)
            .notify(Constants.NOTIFICATION_ID, notificationCompatBuilder.build())
    }
}

fun Context.getNotificationAction(quote: Quote): NotificationCompat.Action {
    val actionFavIntent =
        Intent(applicationContext, FavActionBroadcastReceiver::class.java).apply {
            action = "ACTION_FAV"
            putExtra("quote", Gson().toJson(quote))
        }

    val actionFavPendingIntent: PendingIntent =
        PendingIntent.getBroadcast(
            applicationContext,
            0,
            actionFavIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    val action = NotificationCompat.Action(
        if (quote.liked) R.drawable.heart_filled else R.drawable.heart_unfilled,
        if (quote.liked) "Remove from Favourites" else "Add to Favourites",
        actionFavPendingIntent
    )

    return action
}
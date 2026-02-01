package com.shalenmathew.quotesapp.domain.repository

import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import kotlin.reflect.KClass

interface GlanceWidgetManager {
    suspend fun <T : GlanceAppWidget> getWidgetIds(widgetClass: KClass<T>): List<GlanceId>
    suspend fun <T : GlanceAppWidget> updateAllWidgets(widget: T)
    suspend fun updateWidgetState(
        glanceId: GlanceId,
        updateBlock: (Preferences) -> Preferences
    )
}
package com.shalenmathew.quotesapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.shalenmathew.quotesapp.domain.repository.GlanceWidgetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.reflect.KClass

class GlanceWidgetManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GlanceWidgetManager {

    private val glanceAppWidgetManager = GlanceAppWidgetManager(context)

    override suspend fun <T : GlanceAppWidget> getWidgetIds(
        widgetClass: KClass<T>
    ): List<GlanceId> {
        return glanceAppWidgetManager.getGlanceIds(widgetClass.java)
    }

    override suspend fun updateWidgetState(
        glanceId: GlanceId,
        updateBlock: (Preferences) -> Preferences
    ) {
        updateAppWidgetState(
            context,
            PreferencesGlanceStateDefinition,
            glanceId,
            updateBlock
        )
    }

    override suspend fun <T : GlanceAppWidget> updateAllWidgets(widget: T) {
        widget.updateAll(context)
    }
}
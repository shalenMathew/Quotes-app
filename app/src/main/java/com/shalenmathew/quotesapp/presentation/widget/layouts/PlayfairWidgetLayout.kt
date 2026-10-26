package com.shalenmathew.quotesapp.presentation.widget.layouts

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.shalenmathew.quotesapp.presentation.widget.WidgetTheme

@Composable
fun PlayfairWidgetLayout(savedQuote: String, theme: WidgetTheme) {
    val radius = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        android.R.dimen.system_app_widget_background_radius
    } else {
        null
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .then(
                if (radius != null) GlanceModifier.cornerRadius(radius)
                else GlanceModifier.cornerRadius(16.dp)
            )
            .background(theme.backgroundColor ?: Color(0xFFF1EFE9))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = savedQuote,
            style = TextStyle(
                fontSize = 18.sp,
                color = ColorProvider(theme.quoteTextColor, theme.quoteTextColor),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif
            )
        )
    }
}

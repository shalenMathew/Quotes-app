package com.shalenmathew.quotesapp.presentation.widget.layouts

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.widget.WidgetTheme

@Composable
fun ClassicPrismWidgetLayout(savedQuote: String, quoteId: Int, theme: WidgetTheme) {
    val radius = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        android.R.dimen.system_app_widget_background_radius
    } else {
        null
    }

    val backgroundModifier = if (theme.backgroundResource != null) {
        GlanceModifier.background(ImageProvider(theme.backgroundResource))
    } else {
        GlanceModifier.background(ImageProvider(R.drawable.widget_prism_bg))
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .then(
                if (radius != null) GlanceModifier.cornerRadius(radius)
                else GlanceModifier.cornerRadius(16.dp)
            )
            .then(backgroundModifier)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = GlanceModifier.defaultWeight()
            ) {
                Text(
                    text = "A gentle reminder for today",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = ColorProvider(theme.labelTextColor, theme.labelTextColor),
                        fontWeight = FontWeight.Normal,
                    ),
                    modifier = GlanceModifier.padding(bottom = 8.dp)
                )

                Text(
                    text = savedQuote,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = ColorProvider(theme.quoteTextColor, theme.quoteTextColor),
                        fontWeight = FontWeight.Medium,
                    )
                )
            }

            Spacer(modifier = GlanceModifier.width(15.dp))

            val prismImages = theme.prismImages
            val selectedPrism = if (quoteId != -1) prismImages[Math.abs(quoteId) % prismImages.size] else R.drawable.prism3

            Image(
                provider = ImageProvider(selectedPrism),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = GlanceModifier.size(60.dp)
            )
        }
    }
}

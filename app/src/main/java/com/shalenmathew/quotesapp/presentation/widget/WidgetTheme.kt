package com.shalenmathew.quotesapp.presentation.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.widget.layouts.ClassicPrismWidgetLayout
import com.shalenmathew.quotesapp.presentation.widget.layouts.MinimalWidgetLayout
import com.shalenmathew.quotesapp.presentation.widget.layouts.PlayfairWidgetLayout

data class WidgetTheme(
    val id: String,
    val name: String,
    val backgroundResource: Int? = null,
    val backgroundColor: Color? = null,
    val quoteTextColor: Color,
    val labelTextColor: Color,
    val prismImages: List<Int> = listOf(
        R.drawable.prism, R.drawable.prism2, R.drawable.prism3,
        R.drawable.prism4, R.drawable.prism5, R.drawable.prism6,
        R.drawable.prism7, R.drawable.prism8
    ),
    val contentLayout: @Composable (savedQuote: String, quoteId: Int, theme: WidgetTheme) -> Unit
)

object WidgetThemeRegistry {
    val Default = WidgetTheme(
        id = "default",
        name = "Classic Prism",
        backgroundResource = R.drawable.widget_prism_bg,
        quoteTextColor = Color.White,
        labelTextColor = Color.White.copy(alpha = 0.6f),
        contentLayout = { savedQuote, quoteId, theme ->
            ClassicPrismWidgetLayout(savedQuote, quoteId, theme)
        }
    )

    val AllBlack = WidgetTheme(
        id = "all_black",
        name = "Midnight Black",
        backgroundColor = Color.Black,
        quoteTextColor = Color.White,
        labelTextColor = Color.Gray,
        contentLayout = { savedQuote, _, theme ->
            MinimalWidgetLayout(savedQuote, theme)
        }
    )

    val AllWhite = WidgetTheme(
        id = "all_white",
        name = "Pure White",
        backgroundColor = Color.White,
        quoteTextColor = Color.Black,
        labelTextColor = Color.DarkGray,
        contentLayout = { savedQuote, _, theme ->
            MinimalWidgetLayout(savedQuote, theme)
        }
    )

    val Playfair = WidgetTheme(
        id = "playfair",
        name = "Playfair",
        backgroundColor = Color(0xFFF1EFE9),
        quoteTextColor = Color.Black,
        labelTextColor = Color.Black.copy(alpha = 0.7f),
        contentLayout = { savedQuote, _, theme ->
            PlayfairWidgetLayout(savedQuote, theme)
        }
    )

    val themes = listOf(Default, AllBlack, AllWhite, Playfair)

    fun getTheme(id: String?): WidgetTheme = themes.find { it.id == id } ?: Default
}

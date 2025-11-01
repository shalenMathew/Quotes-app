package com.shalenmathew.quotesapp.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */

)

val Poppins = FontFamily(
    Font(R.font.poppinsbold,FontWeight.Bold),
    Font(R.font.poppinsregular,FontWeight.Medium)
    )

val RobotoFont = FontFamily(
    Font(R.font.roboto_condensed_bold,FontWeight.Bold),
    Font(R.font.roboto_condensed_regular,FontWeight.Normal),
    Font(R.font.roboto_condensed_medium,FontWeight.Medium)
)

val GIFont = FontFamily(
    Font(R.font.glaciaiindifference_bold,FontWeight.Bold),
    Font(R.font.glaciaiindifference_regular,FontWeight.Medium),
    Font(R.font.glaciaiindifference_itallic,FontWeight.Thin)
)

val bratTheme = FontFamily(
    Font(R.font.arialnarrow,FontWeight.Medium),
)

val handWritten = FontFamily(
    Font(R.font.hand_made_bold,FontWeight.Bold),
    Font(R.font.hand_made_medium,FontWeight.Medium),
)


val sugarPie = FontFamily(
    Font(R.font.suagr_pie,FontWeight.Medium),
)


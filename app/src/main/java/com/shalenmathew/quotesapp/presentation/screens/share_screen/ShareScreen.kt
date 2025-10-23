package com.shalenmathew.quotesapp.presentation.screens.share_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.*
import com.shalenmathew.quotesapp.presentation.theme.GIFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController
) {

    val styleRepository = remember { StylePreferenceRepository() }
    var imgBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var quoteStyleState by remember { mutableStateOf<QuoteStyle>(styleRepository.getDefaultQuoteStyle()) }
    var triggerCapture by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<String?>(null) }
    var liquidStartColor by remember { mutableStateOf(Color(0xFFf093fb)) }
    var liquidEndColor by remember { mutableStateOf(Color(0xFF0022BB)) }
    var showColorPicker by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf("start") }

    LaunchedEffect(Unit) {
        quoteStyleState = styleRepository.getDefaultQuoteStyle()
    }

    LaunchedEffect(imgBitmap, pendingAction) {
        imgBitmap?.let { bitmap ->
            when (pendingAction) {
                "download" -> {

                    pendingAction = null
                }
                "share" -> {

                    pendingAction = null
                }
            }
        }
    }

    val quote = navHost.previousBackStackEntry?.savedStateHandle?.get<Quote>("quote")

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .background(color = Color.Black)
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.weight(.9f),
            contentAlignment = Alignment.Center
        ) {
            if (quote != null) {
                CaptureBitmap(
                    triggerCapture = triggerCapture,
                    onCapture = { capturedBitmap ->
                        imgBitmap = capturedBitmap
                        triggerCapture = false
                    }
                ) {
                    when (quoteStyleState) {
                        QuoteStyle.DefaultTheme -> DefaultQuoteCard(Modifier.fillMaxWidth(), quote)
                        QuoteStyle.CodeSnippetTheme -> CodeSnippetStyleQuoteCard(Modifier.fillMaxWidth(), quote)
                        QuoteStyle.bratTheme -> BratScreen(Modifier.fillMaxWidth(), quote)
                        QuoteStyle.igorTheme -> IgorScreen(Modifier.fillMaxWidth(), quote)
                        QuoteStyle.LiquidGlassTheme -> LiquidGlassScreen(
                            modifier = Modifier.fillMaxWidth(),
                            quote = quote,
                            color1 = liquidStartColor,
                            color2 = liquidEndColor
                        )
                        QuoteStyle.ReminderTheme -> ReminderStyle(Modifier.fillMaxWidth(), quote)
                        QuoteStyle.FliplingoesTheme -> FliplingoesTheme(
                            modifier = Modifier.fillMaxWidth(),
                            quote = quote
                        )
                    }
                }
            } else {
                Log.d("TAG", "ShareScreen: quote is null")
                Toast.makeText(context, "quote is null", Toast.LENGTH_SHORT).show()
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.1f)
                .background(color = Color.Black),
            contentAlignment = Alignment.BottomEnd
        ) {
            Row(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(horizontal = 50.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                AnimatedVisibility(
                    visible = quoteStyleState == QuoteStyle.LiquidGlassTheme,

                    modifier = Modifier.weight(1f),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)

                                .background(liquidStartColor)
                                .border(2.dp, Color.White, CircleShape)
                                .clickable {
                                    editTarget = "start"
                                    showColorPicker = true
                                }
                        )
                        Spacer(modifier = Modifier.width(16.dp))


                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)

                                .background(liquidEndColor)
                                .border(2.dp, Color.White, CircleShape)
                                .clickable {
                                    editTarget = "end"
                                    showColorPicker = true
                                }
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.custom),
                    contentDescription = "Themes",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { showSheet = true }
                )

                Image(
                    painter = painterResource(R.drawable.downloads),
                    contentDescription = "Download",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { pendingAction = "download"; triggerCapture = true }
                )

                Image(
                    painter = painterResource(R.drawable.share),
                    contentDescription = "Share",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { pendingAction = "share"; triggerCapture = true }
                )
            }
        }
    }


    @Composable
    fun ThemePreviewCard(
        title: String,
        isSelected: Boolean = false,
        onClick: () -> Unit = {},
        preview: @Composable () -> Unit
    ) {
        Column(
            modifier = Modifier

                .wrapContentSize(Alignment.CenterStart)
                .clickable { onClick() }
                .padding(vertical = 8.dp)

                .padding(start = 7.dp, end = 120.dp),

            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text(
                    text = title,
                    fontFamily = GIFont,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    // Removed .weight(1f) to let the title take natural width
                    modifier = Modifier.padding(start = 8.dp)
                )


            }



            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))


                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {

                preview()


                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4CAF50),
                        uncheckedColor = Color.Gray
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                )
            }
        }
    }


    if (showColorPicker) {
        val initialColor = when (editTarget) {
            "start" -> liquidStartColor
            "end" -> liquidEndColor
            else -> Color.Black
        }

        CustomPickerDialog(
            initialColor = initialColor,
            onSelect = { newColor ->
                if (editTarget == "start") {
                    liquidStartColor = newColor
                } else {
                    liquidEndColor = newColor
                }
            },
            onDismiss = { showColorPicker = false }
        )
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFFF8F8F8)
        ) {
            val previewQuote = Quote(
                id = null,
                quote = "Stay hungry, stay foolish.",
                author = "Steve Jobs",
                liked = false
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Customize Your Quotes",
                    fontSize = 22.sp,
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)

                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    val themes = listOf(
                        "Default Theme" to QuoteStyle.DefaultTheme,
                        "Code Snippet" to QuoteStyle.CodeSnippetTheme,
                        "Brat Theme" to QuoteStyle.bratTheme,
                        "Igor Theme" to QuoteStyle.igorTheme,
                        "Liquid Glass" to QuoteStyle.LiquidGlassTheme,
                        "Reminder Theme" to QuoteStyle.ReminderTheme,
                        "Fliplingoes Theme" to QuoteStyle.FliplingoesTheme
                    )

                    items(themes) { item ->
                        val (title, style) = item
                        val isSelected = style == quoteStyleState

                        ThemePreviewCard(
                            title = title,
                            isSelected = isSelected,
                            onClick = {
                                quoteStyleState = style
                                styleRepository.changeDefaultQuoteStyle(style)
                            }
                        ) {
                            val previewModifier = Modifier.fillMaxSize()
                            when (style) {
                                QuoteStyle.DefaultTheme -> DefaultQuoteCard(modifier = previewModifier, quote = previewQuote)
                                QuoteStyle.CodeSnippetTheme -> CodeSnippetStyleQuoteCard(modifier = previewModifier, quote = previewQuote)
                                QuoteStyle.bratTheme -> BratScreen(modifier = previewModifier, quote = previewQuote)
                                QuoteStyle.igorTheme -> IgorScreen(modifier = previewModifier, quote = previewQuote)
                                QuoteStyle.LiquidGlassTheme -> LiquidGlassScreen(
                                    modifier = previewModifier,
                                    quote = previewQuote,
                                    color1 = liquidStartColor,
                                    color2 = liquidEndColor
                                )
                                QuoteStyle.ReminderTheme -> ReminderStyle(modifier = previewModifier, quote = previewQuote)
                                QuoteStyle.FliplingoesTheme -> FliplingoesTheme(modifier = previewModifier, quote = previewQuote)
                            }
                        }
                    }
                }
            }
        }
    }
}
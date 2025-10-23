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
//    var imgBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    var quoteStyleState by remember { mutableStateOf<QuoteStyle>(QuoteStyle.DefaultTheme) }
    var defaultQuoteStyle by remember { mutableStateOf<QuoteStyle>(QuoteStyle.DefaultTheme) }
//    var triggerCapture by remember { mutableStateOf(false) }
//    var pendingAction by remember { mutableStateOf<String?>(null) }

    var captureRequest by remember { mutableStateOf<String?>(null) }




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

        val defaultStyle = viewModel.getDefaultQuoteStyle()
        quoteStyleState = defaultStyle
        defaultQuoteStyle = defaultStyle
    }

//    LaunchedEffect(imgBitmap, pendingAction) {
//        imgBitmap?.let { bitmap ->
//            when (pendingAction) {
//                "download" -> {
//                    saveImgInGallery(context, bitmap.asAndroidBitmap())
//                    pendingAction = null
//                }
//                "share" -> {
//                    shareImg(context, bitmap.asAndroidBitmap())
//                    pendingAction = null
//                }
//            }
//        }
//    }


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
                    captureRequest = captureRequest,
                    onCapture = { capturedBitmap, action ->
                        when (action) {
                            "download" -> {
                                saveImgInGallery(context, capturedBitmap.asAndroidBitmap())
                            }
                            "share" -> {
                                shareImg(context, capturedBitmap.asAndroidBitmap())
                            }
                        }
                        captureRequest = null
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

                    modifier = Modifier.size(28.dp).clickable {
                        captureRequest = "download"
                    })


                Image(
                    painter = painterResource(R.drawable.share),
                    contentDescription = "Share",
                    colorFilter = ColorFilter.tint(Color.White),

                    modifier = Modifier
                        .size(28.dp)
                        .clickable { pendingAction = "share"; triggerCapture = true }

                    modifier = Modifier.size(28.dp)
                        .clickable {
                            captureRequest = "share"
                        }


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




                    Row(modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()) {

                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {

                            Image(
                                painter = painterResource(R.drawable.sample_code_snippet),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                                    .clickable {
                                        quoteStyleState = QuoteStyle.CodeSnippetTheme
                                        showSheet = false
                                    },
                                contentScale = ContentScale.Fit
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = defaultQuoteStyle == QuoteStyle.CodeSnippetTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        defaultQuoteStyle = QuoteStyle.CodeSnippetTheme
                                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                                    }
                                }
                            )
                        }
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

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {
                            Image(
                                painter = painterResource(R.drawable.sample_brat_theme),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                                    .clickable {
                                        quoteStyleState = QuoteStyle.bratTheme
                                        showSheet = false
                                    },
                                contentScale = ContentScale.Fit
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = defaultQuoteStyle == QuoteStyle.bratTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        defaultQuoteStyle = QuoteStyle.bratTheme
                                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                                    }
                                }
                            )
                        }
                    }



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

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {
                            Image(
                                painter = painterResource(R.drawable.sample_igor),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                                    .clickable {
                                        quoteStyleState = QuoteStyle.igorTheme
                                        showSheet = false
                                    },
                                contentScale = ContentScale.Fit
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = defaultQuoteStyle == QuoteStyle.igorTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        defaultQuoteStyle = QuoteStyle.igorTheme
                                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                                    }
                                }
                            )
                        }
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

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {
                            Image(
                                painter = painterResource(R.drawable.sample_default_style),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                                    .clickable {
                                        quoteStyleState = QuoteStyle.DefaultTheme
                                        showSheet = false
                                    },
                                contentScale = ContentScale.Fit
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = defaultQuoteStyle == QuoteStyle.DefaultTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        defaultQuoteStyle = QuoteStyle.DefaultTheme
                                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                                    }
                                }
                            )
                        }
                    }
                }
                /**  LIQUID GLASS */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {

                    Text(text = "Liquid Glass",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {
                            Image(
                                painter = painterResource(R.drawable.sample_liquid_glass),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                                    .clickable {
                                        quoteStyleState = QuoteStyle.LiquidGlassTheme
                                        showSheet = false
                                    },
                                contentScale = ContentScale.Fit
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = defaultQuoteStyle == QuoteStyle.LiquidGlassTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        defaultQuoteStyle = QuoteStyle.LiquidGlassTheme
                                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                                    }
                                }
                            )
                        }
                    }
                }

                /* REMINDER THEME */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight())
                {

                    Text(text = "Reminder theme",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 5.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

//                    Row(modifier = Modifier.fillMaxWidth()
//                        .wrapContentHeight())
//                    {
//
//                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {
//
//                            ReminderStyleCover(
//                                modifier = Modifier.size(200.dp).background(Color.Blue)
//                                .clickable {
//                                    quoteStyleState = QuoteStyle.ReminderTheme
//                                    showSheet = false
//                                },
//                            )
//                            Checkbox(
//                                modifier = Modifier.align(Alignment.BottomEnd),
//                                checked = quoteStyleState == QuoteStyle.ReminderTheme,
//                                onCheckedChange = { isChecked ->
//                                    if (isChecked) {
//                                        quoteStyleState = QuoteStyle.ReminderTheme
//                                        viewModel.changeDefaultQuoteStyle(quoteStyleState)
//                                    }
//                                }
//                            )
//                        }
//                    }

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {
                            Image(
                                painter = painterResource(R.drawable.sample_reminder_2),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                                    .clickable {
                                        quoteStyleState = QuoteStyle.ReminderTheme
                                        showSheet = false
                                    },
                                contentScale = ContentScale.Crop
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = defaultQuoteStyle == QuoteStyle.ReminderTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        defaultQuoteStyle = QuoteStyle.ReminderTheme
                                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                                    }
                                }
                            )

                        }
                    }
                }
            }
        }
    }
}
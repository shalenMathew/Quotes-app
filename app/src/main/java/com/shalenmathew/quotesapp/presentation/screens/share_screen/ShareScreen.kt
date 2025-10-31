package com.shalenmathew.quotesapp.presentation.screens.share_screen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.FliplingoesTheme
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.BratScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.CaptureBitmap
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.CodeSnippetStyleQuoteCard
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.DiceDreamsStyleQuoteCard
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.DefaultQuoteCard
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.IgorScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.LiquidGlassScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.MinimalBlackTheme
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.MinimalBrownTheme
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.ReminderStyle
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.TravelCardTheme
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.viewmodel.ShareQuoteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    viewModel: ShareQuoteViewModel= hiltViewModel()
) {


    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    var quoteStyleState by remember { mutableStateOf<QuoteStyle>(QuoteStyle.DefaultTheme) }
    var defaultQuoteStyle by remember { mutableStateOf<QuoteStyle>(QuoteStyle.DefaultTheme) }
    var captureRequest by remember { mutableStateOf<String?>(null) }
    var liquidStartColor by remember { mutableStateOf(Color(0xFFf093fb)) }
    var liquidEndColor by remember { mutableStateOf(Color(0xFF0022BB)) }
    var diceDreamColor by remember { mutableStateOf(Color(0xFF0022BB)) }
    var showColorPicker by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf("start") }

    var travelImageUri by remember { mutableStateOf<android.net.Uri?>("https://images.unsplash.com/photo-1708784092854-bMIlyKZHKMY?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80".toUri()) }
    val pickTravelImage = rememberLauncherForActivityResult( contract = ActivityResultContracts.GetContent() ) { uri -> travelImageUri = uri }

    var diceDreamsImageUri by remember { mutableStateOf<android.net.Uri?>("https://images.unsplash.com/photo-1708784092854-bMIlyKZHKMY?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80".toUri()) }
    val pickDiceImage = rememberLauncherForActivityResult( contract = ActivityResultContracts.GetContent() ) { uri -> diceDreamsImageUri = uri }



    LaunchedEffect(Unit) {
        val defaultStyle = viewModel.getDefaultQuoteStyle()
        quoteStyleState = defaultStyle
        defaultQuoteStyle = defaultStyle
    }

    val quote = navHost.previousBackStackEntry?.savedStateHandle?.get<Quote>("quote")

    Column (
        modifier = Modifier.padding(paddingValues)
            .background(color = Color.Black)
            .fillMaxSize(),
    )
    {

        Box(modifier = Modifier
            .weight(.9f),
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
                    // All style rendering happens here with access to ShareScreen's state
                    when (quoteStyleState) {
                        QuoteStyle.DefaultTheme -> DefaultQuoteCard(Modifier, quote)
                        QuoteStyle.CodeSnippetTheme -> CodeSnippetStyleQuoteCard(Modifier, quote)
                        QuoteStyle.DiceDreamsTheme -> DiceDreamsStyleQuoteCard(
                            Modifier, quote, color = diceDreamColor, imageUri = diceDreamsImageUri, onPickImage = { pickDiceImage.launch("image/*")}
                        )
                        QuoteStyle.bratTheme -> BratScreen(Modifier, quote)
                        QuoteStyle.igorTheme -> IgorScreen(Modifier, quote)
                        QuoteStyle.LiquidGlassTheme -> LiquidGlassScreen(
                            modifier = Modifier,
                            quote = quote,
                            color1 = liquidStartColor,  // from ShareScreen state
                            color2 = liquidEndColor     // from ShareScreen state
                        )
                        QuoteStyle.FliplingoesTheme -> FliplingoesTheme(quote = quote)

                        QuoteStyle.ReminderTheme -> ReminderStyle(Modifier, quote)

                        QuoteStyle.TravelCardTheme -> TravelCardTheme(
                            modifier = Modifier, quote = quote, imageUri = travelImageUri, onPickImage = { pickTravelImage.launch("image/*") }
                        )

                        QuoteStyle.MinimalBlackTheme -> MinimalBlackTheme(quote = quote)
                        QuoteStyle.MinimalBrownTheme -> MinimalBrownTheme(quote = quote)                    }
                }

            } else {
                Log.d("TAG", "ShareScreen: quote is null")
                Toast.makeText(context, "quote is null", Toast.LENGTH_SHORT).show()
            }

        }

        Box(
            modifier = Modifier.fillMaxWidth()
                .weight(.1f)
                .background(color = Color.Black),
            contentAlignment = Alignment.BottomEnd
        )
        {

            Row(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(horizontal = 50.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                AnimatedVisibility(
                    visible = quoteStyleState == QuoteStyle.LiquidGlassTheme
                ) {
                    Row {
                        IconButton(
                            onClick = {
                                editTarget = "start"
                                showColorPicker = true

                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = liquidStartColor
                            ),
                            content = {}
                        )

                        IconButton(
                            onClick = {
                                editTarget = "end"
                                showColorPicker = true
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = liquidEndColor
                            ),
                            content = {}
                        )
                    }
                }

                AnimatedVisibility(visible = quoteStyleState == QuoteStyle.DiceDreamsTheme) {
                    Row {
                        IconButton(
                            onClick = {
                                editTarget = "diceDreamsColor"
                                showColorPicker = true

                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = diceDreamColor
                            ),
                            content = {},
                            modifier = Modifier.padding(10.dp,0.dp)
                        )

                        Image(
                            painter = painterResource(R.drawable.upload),
                            contentDescription = "Upload image",
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    pickDiceImage.launch("image/*")
                                }
                        )
                    }
                }

                AnimatedVisibility(visible = quoteStyleState == QuoteStyle.TravelCardTheme) {
                    Image(
                        painter = painterResource(R.drawable.upload),
                        contentDescription = "Upload image",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                pickTravelImage.launch("image/*")
                            }
                    )
                }

                Image(
                    painter = painterResource(R.drawable.custom), contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(28.dp)
                        .clickable {
                            showSheet = true
                        })

                Image(
                    painter = painterResource(R.drawable.downloads), contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(28.dp).clickable {
                        captureRequest = "download"
                    })

                Image(
                    painter = painterResource(R.drawable.share), contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(28.dp)
                        .clickable {
                            captureRequest = "share"
                        }

                )

            }

        }

    }


    // COLOR PICKER DIALOG
    if (showColorPicker){
        CustomPickerDialog(
            initialColor = if (editTarget == "start") liquidStartColor else if (editTarget == "end") liquidEndColor else diceDreamColor,
            onSelect = { selectedColor ->
                if (editTarget == "start") {
                    liquidStartColor = selectedColor
                } else if (editTarget == "end")  {
                    liquidEndColor = selectedColor
                } else  {
                    diceDreamColor = selectedColor
                }
            },
            onDismiss = { showColorPicker = false }
        )
    }

    // BOTTOM SHEET
    if (showSheet) {

        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.LightGray
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Customize Your Quotes",
                    fontSize = 25.sp,
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 15.dp)
                )

                // Code Snippet Theme
                ThemeItem(
                    title = "Code Snippet",
                    drawableRes = R.drawable.sample_code_snippet,
                    quoteStyle = QuoteStyle.CodeSnippetTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.CodeSnippetTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.CodeSnippetTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.CodeSnippetTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                // Dice Dreams Theme
                ThemeItem(
                    title = "Dice Dreams",
                    drawableRes = R.drawable.sample_dice_dreams,
                    quoteStyle = QuoteStyle.DiceDreamsTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.DiceDreamsTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.DiceDreamsTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.DiceDreamsTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                // brat Theme
                ThemeItem(
                    title = "brat Theme",
                    drawableRes = R.drawable.sample_brat_theme,
                    quoteStyle = QuoteStyle.bratTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.bratTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.bratTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.bratTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                // IGOR Theme
                ThemeItem(
                    title = "IGOR Theme",
                    drawableRes = R.drawable.sample_igor,
                    quoteStyle = QuoteStyle.igorTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.igorTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.igorTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.igorTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                // Default Theme
                ThemeItem(
                    title = "Default Theme",
                    drawableRes = R.drawable.sample_default_style,
                    quoteStyle = QuoteStyle.DefaultTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.DefaultTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.DefaultTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.DefaultTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                // Liquid Glass Theme
                ThemeItem(
                    title = "Liquid Glass",
                    drawableRes = R.drawable.sample_liquid_glass,
                    quoteStyle = QuoteStyle.LiquidGlassTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.LiquidGlassTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.LiquidGlassTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.LiquidGlassTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                // Reminder Theme
                ThemeItem(
                    title = "Reminder theme",
                    drawableRes = R.drawable.sample_reminder_2,
                    quoteStyle = QuoteStyle.ReminderTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.ReminderTheme,
                    contentScale = ContentScale.Crop,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.ReminderTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.ReminderTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                ThemeItem(
                    title = "Travel Card Theme",
                    drawableRes = R.drawable.sample_travel_card,
                    quoteStyle = QuoteStyle.TravelCardTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.TravelCardTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.TravelCardTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.TravelCardTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                ThemeItem(
                    title = "Fliplingoes Theme",
                    drawableRes = R.drawable.sample_fliplingoes,
                    quoteStyle = QuoteStyle.FliplingoesTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.FliplingoesTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.FliplingoesTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.FliplingoesTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                ThemeItem(
                    title = "Minimal Black Theme",
                    drawableRes = R.drawable.sample_minimal_black,
                    quoteStyle = QuoteStyle.MinimalBlackTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.MinimalBlackTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.MinimalBlackTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.MinimalBlackTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                ThemeItem(
                    title = "Minimal Brown Theme",
                    drawableRes = R.drawable.samplebrown,
                    quoteStyle = QuoteStyle.MinimalBrownTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.MinimalBrownTheme,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.MinimalBrownTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.MinimalBrownTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

            }
        }
    }
}


@Composable
fun ThemeItem(
    title: String,
    drawableRes: Int,
    quoteStyle: QuoteStyle,
    isSelected: Boolean,
    contentScale: ContentScale = ContentScale.Crop,
    onThemeClick: () -> Unit,
    onSetDefault: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 10.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 10.dp),
            fontFamily = GIFont,
            fontWeight = FontWeight.Medium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {
                Image(
                    painter = painterResource(drawableRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { onThemeClick() },
                    contentScale = contentScale
                )
                Checkbox(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    checked = isSelected,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            onSetDefault()
                        }
                    }
                )
            }
        }
    }
}


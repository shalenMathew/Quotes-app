package com.shalenmathew.quotesapp.presentation.screens.share_screen


import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.BratScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.CaptureBitmap
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.CodeSnippetStyleQuoteCard
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.DefaultQuoteCard
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.IgorScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.LiquidGlassScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.ReminderStyle
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.CardImageStyle
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.ArtisanCardStyle
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
    var showColorPicker by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf("start") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

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
                        QuoteStyle.bratTheme -> BratScreen(Modifier, quote)
                        QuoteStyle.igorTheme -> IgorScreen(Modifier, quote)
                        QuoteStyle.LiquidGlassTheme -> LiquidGlassScreen(
                            modifier = Modifier,
                            quote = quote,
                            color1 = liquidStartColor,  // from ShareScreen state
                            color2 = liquidEndColor     // from ShareScreen state
                        )
                        QuoteStyle.CardImageTheme -> CardImageStyle(Modifier, quote, selectedImageUri)
                        QuoteStyle.ArtisanCardTheme -> ArtisanCardStyle(Modifier, quote, selectedImageUri)
                        QuoteStyle.ReminderTheme -> ReminderStyle(Modifier, quote)
                    }
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

                Image(
                    painter = painterResource(R.drawable.custom), contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(28.dp)
                        .clickable {
                            showSheet = true
                        })

                // When Card Image or Artisan Card theme is active, show an icon to pick an image from gallery
                if (quoteStyleState == QuoteStyle.CardImageTheme || quoteStyleState == QuoteStyle.ArtisanCardTheme) {
                    Image(
                        painter = painterResource(R.drawable.sample3),
                        contentDescription = "Pick background image",
                        modifier = Modifier.size(28.dp)
                            .clickable {
                                // Launch image picker
                                imagePickerLauncher.launch("image/*")
                            }
                    )
                }

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
            initialColor = if (editTarget == "start") liquidStartColor else liquidEndColor,
            onSelect = { selectedColor ->
                if (editTarget == "start") {
                    liquidStartColor = selectedColor
                } else {
                    liquidEndColor = selectedColor
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

                // Card Image Theme (new)
                ThemeItem(
                    title = "Card Image",
                    drawableRes = R.drawable.sample3,
                    quoteStyle = QuoteStyle.CardImageTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.CardImageTheme,
                    contentScale = ContentScale.Crop,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.CardImageTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.CardImageTheme
                        viewModel.changeDefaultQuoteStyle(defaultQuoteStyle)
                    }
                )

                // Artisan Card Theme (new)
                ThemeItem(
                    title = "Artisan Card",
                    drawableRes = R.drawable.sample3,
                    quoteStyle = QuoteStyle.ArtisanCardTheme,
                    isSelected = defaultQuoteStyle == QuoteStyle.ArtisanCardTheme,
                    contentScale = ContentScale.Crop,
                    onThemeClick = {
                        quoteStyleState = QuoteStyle.ArtisanCardTheme
                        showSheet = false
                    },
                    onSetDefault = {
                        defaultQuoteStyle = QuoteStyle.ArtisanCardTheme
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
    contentScale: ContentScale = ContentScale.Fit,
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



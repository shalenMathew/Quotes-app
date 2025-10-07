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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.viewmodel.ShareQuoteUiAction
import com.shalenmathew.quotesapp.presentation.viewmodel.ShareQuoteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    viewModel: ShareQuoteViewModel= hiltViewModel()
) {

    val context = LocalContext.current
    var imgBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    var triggerCapture by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<String?>(null) }

    var liquidStartColor by remember { mutableStateOf(Color(0xFFf093fb)) }
    var liquidEndColor by remember { mutableStateOf(Color(0xFF0022BB)) }

    var showColorPicker by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf("start") }

    LaunchedEffect(imgBitmap, pendingAction) {
        imgBitmap?.let { bitmap ->
            when (pendingAction) {
                "download" -> {
                    saveImgInGallery(context, bitmap.asAndroidBitmap())
                    pendingAction = null
                }

                "share" -> {
                    shareImg(context, bitmap.asAndroidBitmap())
                    pendingAction = null
                }
            }
        }
    }

    val quote = navHost.previousBackStackEntry?.savedStateHandle?.get<Quote>("quote")

    Box(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .background(color = Color.Black)
            .fillMaxSize(),
    ) {

        Box(modifier = Modifier
            .wrapContentSize()
            .align(Alignment.Center)) {

//            Spacer(modifier = Modifier.weight(1f))

            if (quote != null) {
//                CaptureBitmap(quoteData = quote,
//                    quoteStyleState,
//                    triggerCapture = triggerCapture
//                ) { capturedBitmap ->
//
//                    imgBitmap = capturedBitmap
//                    triggerCapture = false
//                }

                CaptureBitmap(
                    triggerCapture = triggerCapture,
                    onCapture = { capturedBitmap ->
                        imgBitmap = capturedBitmap
                        triggerCapture = false
                    }
                ) {
                    // All style rendering happens here with access to ShareScreen's state
                    when (state.displayStyle) {
                        QuoteStyle.DEFAULT_THEME -> DefaultQuoteCard(quote = quote)
                        QuoteStyle.CODE_SNIPPET_THEME -> CodeSnippetStyleQuoteCard(quote = quote)
                        QuoteStyle.LIQUID_GLASS_THEME -> LiquidGlassScreen(
                            quote = quote,
                            color1 = liquidStartColor,  // from ShareScreen state
                            color2 = liquidEndColor     // from ShareScreen state
                        )

                        QuoteStyle.BRAT_THEME -> BratScreen(quote = quote)
                        QuoteStyle.IGOR_THEME -> IgorScreen(quote = quote)
                        QuoteStyle.REMINDER_THEME -> ReminderStyle(quote = quote)
                    }
                }

            } else {
                Log.d("TAG", "ShareScreen: quote is null")
                Toast.makeText(context, "quote is null", Toast.LENGTH_SHORT).show()
            }

//            Spacer(modifier = Modifier.weight(1f))

        }

        Box(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.Black)
                .align(Alignment.BottomEnd),
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
                    visible = state.displayStyle == QuoteStyle.LIQUID_GLASS_THEME
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
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            viewModel.onAction(ShareQuoteUiAction.ShowStylePicker)
                        })

                Image(
                    painter = painterResource(R.drawable.downloads), contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(28.dp).clickable {
                        pendingAction = "download"
                        triggerCapture = true
                    })

                Image(
                    painter = painterResource(R.drawable.share), contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(28.dp)
                        .clickable {
                            pendingAction = "share"
                            triggerCapture = true
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
    if (state.openStylePicker) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onAction(ShareQuoteUiAction.DismissPicker) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.LightGray,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
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

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        QuoteStyle.entries.size,
                        key = { index -> QuoteStyle.entries[index].title  },
                    ) { index ->
                        val theme = QuoteStyle.entries[index]
                        ThemeSelectorBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            themeTitle = theme.title,
                            themeImage = painterResource(theme.image),
                            isDefault = theme == state.defaultTheme,
                            onApplyStyle = {
                                viewModel.onAction(ShareQuoteUiAction.UpdateDisplayStyle(theme))
                            },
                            setDefaultStyle = {
                                viewModel.onAction(ShareQuoteUiAction.UpdateDefaultStyle(theme))
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ThemeSelectorBox(
    modifier: Modifier = Modifier,
    themeTitle: String,
    themeImage: Painter,
    isDefault: Boolean,
    onApplyStyle: () -> Unit,
    setDefaultStyle: () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = themeTitle,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 5.dp),
            fontFamily = GIFont,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box {
                Image(
                    painter = themeImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(onClick = onApplyStyle),
                    contentScale = ContentScale.Fit
                )
                Checkbox(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    checked = isDefault,
                    onCheckedChange = { isChecked ->
                        if (isChecked) setDefaultStyle()
                    }
                )
            }
        }
    }
}
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
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.CaptureBitmap
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.BratScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.CodeSnippetStyleQuoteCard
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.DefaultQuoteCard
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.IgorScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme.LiquidGlassScreen
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

    // Travel card image state + launcher
    var travelImageUri by remember { mutableStateOf<android.net.Uri?>("https://images.unsplash.com/photo-1708784092854-bMIlyKZHKMY?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80".toUri()) }
    val pickTravelImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        travelImageUri = uri
    }

    LaunchedEffect(Unit) {
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


    val quote = navHost
        .previousBackStackEntry?.savedStateHandle?.get<Quote>("quote") ?: Quote(quote = "Pausing for a moment to look to inspiring leaders", author = "Unknown", liked = true)

    Column (
        modifier = Modifier.padding(paddingValues)
            .background(color = Color.Black)
            .fillMaxSize(),
    ) {

        Box(modifier = Modifier
            .weight(.9f),
            contentAlignment = Alignment.Center
        ) {

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
                    QuoteStyle.ReminderTheme -> ReminderStyle(Modifier, quote)
                    QuoteStyle.TravelCardTheme -> TravelCardTheme(
                        modifier = Modifier,
                        quote = quote,
                        imageUri = travelImageUri,
                        onPickImage = { pickTravelImage.launch("image/*") }
                    )

                }
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
//                            shape = MaterialTheme.shapes.extraLarge,
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
            onDismissRequest = {showSheet=false},
            sheetState = sheetState,
            containerColor = Color.LightGray)
        {

            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .verticalScroll(scrollState)
                ,horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(text = "Customize Your Quotes",
                    fontSize = 25.sp,
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier= Modifier.padding(bottom = 15.dp))


                /**  CODE SNIPPET STYLE*/
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight())
                {

                    Text(text = "Code Snippet",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 5.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

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

                }

                /**  BRAT THEME  */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {

                    Text(text = "brat Theme",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

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

                }

                    /**  IGOR THEME */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {

                    Text(text = "IGOR Theme",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

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

                }

                /**  DEFAULT STYLE*/
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {

                    Text(text = "Default Theme",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

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

                /**  TRAVEL CARD THEME */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {
                    Text(text = "Travel Theme",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {
                            Image(
                                painter = painterResource(R.drawable.travelcard),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                                    .clickable {
                                        quoteStyleState = QuoteStyle.TravelCardTheme
                                        showSheet = false
                                    },
                                contentScale = ContentScale.Fit
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = defaultQuoteStyle == QuoteStyle.TravelCardTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        defaultQuoteStyle = QuoteStyle.TravelCardTheme
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

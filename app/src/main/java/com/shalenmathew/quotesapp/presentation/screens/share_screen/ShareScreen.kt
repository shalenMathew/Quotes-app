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
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.DiceDreamsStyleQuoteCard
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.IgorScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.LiquidGlassScreen
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.ReminderStyle
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.viewmodel.ShareQuoteViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts


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

    var showImage by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var liquidStartColor by remember { mutableStateOf(Color(0xFFf093fb)) }
    var liquidEndColor by remember { mutableStateOf(Color(0xFF0022BB)) }
    var backgroundColor by remember { mutableStateOf(Color(0xFFf093fb))}
    var fontColor by remember { mutableStateOf(Color(0xFF12017B))}

    var showColorPicker by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf("start") }

    // Launcher to pick image from gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            showImage = false // hide the picker trigger
        }
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


    val quote = navHost.previousBackStackEntry?.savedStateHandle?.get<Quote>("quote")

    Column (
        modifier = Modifier.padding(paddingValues)
            .background(color = Color.Black)
            .fillMaxSize(),
    ) {

        Box(modifier = Modifier
            .weight(.9f),
            contentAlignment = Alignment.Center
        ) {

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
                        QuoteStyle.DiceDreamsTheme -> DiceDreamsStyleQuoteCard(Modifier, quote, backgroundColor, fontColor, selectedImageUri = selectedImageUri) // need to be changes
                        QuoteStyle.bratTheme -> BratScreen(Modifier, quote)
                        QuoteStyle.igorTheme -> IgorScreen(Modifier, quote)
                        QuoteStyle.LiquidGlassTheme -> LiquidGlassScreen(
                            modifier = Modifier,
                            quote = quote,
                            color1 = liquidStartColor,  // from ShareScreen state
                            color2 = liquidEndColor     // from ShareScreen state
                        )
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
                    visible = quoteStyleState == QuoteStyle.DiceDreamsTheme
                ) {
                    Image(
                        painter = painterResource(R.drawable.photo), contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(30.dp)
                            .clickable {
                                showImage = true
                            })
                }

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

                AnimatedVisibility(
                    visible = quoteStyleState == QuoteStyle.DiceDreamsTheme
                ) {
                    Row {
                        IconButton(
                            onClick = {
                                editTarget = "backgroundColor"
                                showColorPicker = true
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = backgroundColor
                            ),
                            content = {}
                        )

                        IconButton(
                            onClick = {
                                editTarget = "fontColor"
                                showColorPicker = true
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = fontColor
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
            initialColor = if (editTarget == "start") {
                liquidStartColor
            } else if (editTarget == "end"){
                liquidEndColor
            } else if (editTarget == "backgroundColor"){
                backgroundColor
            } else {
                fontColor
            },
            onSelect = { selectedColor ->
                if (editTarget == "start") {
                    liquidStartColor = selectedColor
                } else if (editTarget == "end") {
                    liquidEndColor = selectedColor
                } else if (editTarget == "backgroundColor") {
                    backgroundColor = selectedColor
                } else {
                    fontColor = selectedColor
                }
            },
            onDismiss = { showColorPicker = false }
        )
    }

    if(showImage) {
        // Launch the gallery picker
        LaunchedEffect(Unit) {
            imagePickerLauncher.launch("image/*")
        }
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

                /**  DICE DREAMS */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight())
                {

                    Text(text = "Dice Dreams",
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
                                painter = painterResource(R.drawable.sample_dice_dreams),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                                    .clickable {
                                        quoteStyleState = QuoteStyle.DiceDreamsTheme
                                        showSheet = false
                                    },
                                contentScale = ContentScale.Fit
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = defaultQuoteStyle == QuoteStyle.DiceDreamsTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        defaultQuoteStyle = QuoteStyle.DiceDreamsTheme
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



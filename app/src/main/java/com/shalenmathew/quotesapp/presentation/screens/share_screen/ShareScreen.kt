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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.CaptureBitmap
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.viewmodel.ShareQuoteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    viewModel: ShareQuoteViewModel= hiltViewModel()
) {

    var imgBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    var quoteStyleState by remember { mutableStateOf<QuoteStyle>(QuoteStyle.DefaultTheme) }
    var triggerCapture by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        quoteStyleState = viewModel.getDefaultQuoteStyle()
    }

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
                CaptureBitmap(quoteData = quote,
                    quoteStyleState,
                    triggerCapture = triggerCapture
                ) { capturedBitmap ->

                    imgBitmap = capturedBitmap
                    triggerCapture = false
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
                    visible = true
                ) {
                    Row {
                        IconButton(
                            onClick = {

                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Red
                            ),
//                            shape = MaterialTheme.shapes.extraLarge,
                            content = {}
                        )

                        IconButton(
                            onClick = {

                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.White
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

                // HACKTOBER FEST

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
                                checked = quoteStyleState == QuoteStyle.CodeSnippetTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        quoteStyleState = QuoteStyle.CodeSnippetTheme
                                        viewModel.changeDefaultQuoteStyle(quoteStyleState)
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
                                checked = quoteStyleState == QuoteStyle.bratTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        quoteStyleState = QuoteStyle.bratTheme
                                        viewModel.changeDefaultQuoteStyle(quoteStyleState)
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
                                checked = quoteStyleState == QuoteStyle.igorTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        quoteStyleState = QuoteStyle.igorTheme
                                        viewModel.changeDefaultQuoteStyle(quoteStyleState)
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
                                checked = quoteStyleState == QuoteStyle.DefaultTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        quoteStyleState = QuoteStyle.DefaultTheme
                                        viewModel.changeDefaultQuoteStyle(quoteStyleState)
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
                                checked = quoteStyleState == QuoteStyle.LiquidGlassTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        quoteStyleState = QuoteStyle.LiquidGlassTheme
                                        viewModel.changeDefaultQuoteStyle(quoteStyleState)
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

                    Row(modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()) {

                        Box(modifier = Modifier.clip(shape = RoundedCornerShape(6))) {

                            ReminderStyleCover(
                                modifier = Modifier.size(200.dp).background(Color.Blue)
                                .clickable {
                                    quoteStyleState = QuoteStyle.ReminderTheme
                                    showSheet = false
                                },
                            )
                            Checkbox(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                checked = quoteStyleState == QuoteStyle.ReminderTheme,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        quoteStyleState = QuoteStyle.ReminderTheme
                                        viewModel.changeDefaultQuoteStyle(quoteStyleState)
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

@Composable
fun ReminderStyleCover (
    modifier: Modifier = Modifier.size(200.dp),
    quote: Quote = Quote(quote = "Pausing for a moment to look to inspiring leaders", author = "Unknown", liked = true),
    bgColor: Color = Color.LightGray,
    textColor: Color = Color(0xFF4B6AD1),
    cardBgColor : Color = Color(0xFFE3EDFD)
){
    Box(

        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.8f)
            .background(bgColor)
            .wrapContentSize(Alignment.Center) ,
        contentAlignment = Alignment.Center
    ){

        Column(modifier = Modifier
            .fillMaxWidth(.8f)
            .wrapContentHeight()
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(cardBgColor)
        ) {

            Text(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 2.dp),
                text = "Reminder" ,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 5.dp),
                text = quote.quote ,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )

            HorizontalDivider()

            Text(text = "Okay" ,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                color = textColor,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


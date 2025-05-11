package com.shalenmathew.quotesapp.presentation.screens.share_screen


import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.share_screen.components.CaptureBitmap
import com.shalenmathew.quotesapp.presentation.theme.GIFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(paddingValues: PaddingValues, navHost: NavHostController) {

    var imgBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    var quoteStyleState by remember { mutableStateOf<QuoteStyle>(QuoteStyle.DefaultTheme) }


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
                CaptureBitmap(quoteData = quote,quoteStyleState) { capturedBitmap ->
                    // saving the captured bitmap
                    imgBitmap = capturedBitmap
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
        ) {

            Row(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(horizontal = 50.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {

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

                        imgBitmap?.let {
                            saveImgInGallery(context, it.asAndroidBitmap())
                        } ?: run {
                            Toast.makeText(context, "No image to save", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("TAG", "ShareScreen: imgBitmap is null")
                        }

                    })

                Image(
                    painter = painterResource(R.drawable.share), contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(28.dp)
                        .clickable {
                            imgBitmap?.let {
                                shareImg(context, it.asAndroidBitmap())
                            } ?: run {
                                Toast.makeText(context, "No image to share", Toast.LENGTH_SHORT)
                                    .show()
                            }
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

                Text(text = "Custom Quotes Style",
                    fontSize = 25.sp,
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier= Modifier.padding(bottom = 15.dp))


                /**  CODE SNIPPET STYLE*/
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight())
                {

                    Text(text = "Code Snippet Style",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 5.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

                    Row(modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()) {

                        Image(painter = painterResource(R.drawable.sample_code_snippet),
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .clickable{
                                    quoteStyleState = QuoteStyle.CodeSnippetTheme
                                    showSheet=false
                                },
                            contentScale = ContentScale.Fit)
                    }

                }

                /**  BRAT THEME  */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {

                    Text(text = "brat Theme Style",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Image(painter = painterResource(R.drawable.sample_brat_theme),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                                .clickable{
                                    quoteStyleState = QuoteStyle.bratTheme
                                    showSheet=false
                                },
                            contentScale = ContentScale.Fit)
                    }

                }

                    /**  IGOR THEME */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {

                    Text(text = "IGOR Theme Style",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Image(painter = painterResource(R.drawable.sample_igor),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                                .clickable{
                                    quoteStyleState = QuoteStyle.igorTheme
                                    showSheet=false
                                },
                            contentScale = ContentScale.Fit)
                    }

                }


                /**  SPOTIFY THEME  STYLE */
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {

                    Text(text = "Spotify Theme Style",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Image(painter = painterResource(R.drawable.sample_spotify_theme),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                                .clickable{
                                    quoteStyleState = QuoteStyle.SpotifyTheme
                                    showSheet=false
                                },
                            contentScale = ContentScale.Fit)
                    }

                }

                /**  DEFAULT STYLE*/
                Column(modifier= Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 10.dp))
                {

                    Text(text = "Default Style",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        Image(painter = painterResource(R.drawable.sample_default_style),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                                .clickable{
                                    quoteStyleState = QuoteStyle.DefaultTheme
                                    showSheet=false
                                },
                            contentScale = ContentScale.Fit)
                    }
                }
            }

        }
    }



}

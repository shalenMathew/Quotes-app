package com.example.quotesapp.presentation.screens.share_screen


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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quotesapp.R
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.presentation.screens.share_screen.components.CaptureBitmap
import com.example.quotesapp.presentation.screens.share_screen.components.CodeSnippetStyleQuoteCard


@Composable
fun ShareScreen(paddingValues: PaddingValues, navHost: NavHostController) {

    var imgBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current

    val quote = navHost.previousBackStackEntry?.savedStateHandle?.get<Quote>("quote")

    Box(modifier = Modifier
        .padding(paddingValues = paddingValues)
        .background(color = Color.Black)
        .fillMaxSize(),
    ) {

        Column(modifier = Modifier.fillMaxSize()){

            Spacer(modifier = Modifier.weight(1f))

            if (quote != null){
                CaptureBitmap(quoteData = quote) { capturedBitmap->
                    // saving the captured bitmap
                    imgBitmap = capturedBitmap
                }
            }else{
                Log.d("TAG", "ShareScreen: quote is null")
                Toast.makeText(context, "quote is null", Toast.LENGTH_SHORT).show()
            }

            Spacer(modifier = Modifier.weight(1f))


            Box(modifier = Modifier.fillMaxWidth()
                .wrapContentHeight(),
                contentAlignment = Alignment.BottomEnd
            ) {

                Row(modifier=Modifier
                    .background(Color.Black)
                    .padding(horizontal = 50.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)){

                    Image(painter = painterResource(R.drawable.custom)
                        ,contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(28.dp)
                            .clickable{
//                                CodeSnippetStyleQuoteCard()
                            })

                    Image(painter = painterResource(R.drawable.downloads)
                        ,contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(28.dp).clickable{

                            imgBitmap?.let {
                                saveImgInGallery(context, it.asAndroidBitmap())
                            }?:run {
                                Toast.makeText(context, "No image to save", Toast.LENGTH_SHORT).show()
                                Log.d("TAG", "ShareScreen: imgBitmap is null")
                            }

                        })

                    Image(painter = painterResource(R.drawable.share)
                        ,contentDescription = null,
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

    }

}

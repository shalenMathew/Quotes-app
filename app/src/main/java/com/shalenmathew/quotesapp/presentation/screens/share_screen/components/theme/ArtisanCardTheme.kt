package com.shalenmathew.quotesapp.presentation.screens.share_screen.components.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote

@Composable
fun ArtisanCardTheme(modifier: Modifier, quote: Quote, imageModel: Any?) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFE9E9EA)) // light grey background for the screen
            .padding(12.dp), contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 30.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.5f),
                    spotColor = Color.Black.copy(alpha = 0.5f)
                ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2077FF)) // bright blue card
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // top image area (rounded inside card)
                AsyncImage(
                    model = imageModel ?: R.drawable.sample_yt_2,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )

                // content area with quote and author
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {

                    Text(
                        text = quote.quote.uppercase(),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                        modifier = Modifier
                            .align(Alignment.Start)
                    )

                    Text(
                        text = quote.author,
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(12.dp))


                    Image(
                        painter = painterResource(id = R.drawable.barcode),
                        contentDescription = null,
                        modifier = Modifier
                            .height(28.dp)
                            .width(160.dp),
                        alignment = Alignment.BottomStart
                    )
                }
            }
        }
    }
}
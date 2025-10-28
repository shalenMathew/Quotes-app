package com.shalenmathew.quotesapp.presentation.screens.share_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.screens.share_screen.darken
import com.shalenmathew.quotesapp.presentation.screens.share_screen.lighten
import com.shalenmathew.quotesapp.presentation.theme.DarkerGrey
import com.shalenmathew.quotesapp.presentation.theme.Violet
import com.shalenmathew.quotesapp.presentation.theme.bratGreen
import com.shalenmathew.quotesapp.presentation.theme.bratTheme
import com.shalenmathew.quotesapp.presentation.theme.handWritten
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild

/**  THIS SECTION COMPRISES OF ALL DIFFERENT STYLES OF QUOTES */

 /** DEFAULT STYLE */
//@Preview
@Composable
fun DefaultQuoteCard(modifier: Modifier, quote: Quote) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Black, Color(0xFF383838)),
                        center = Offset.Unspecified,
                        radius = 1000f
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.quotation),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 20.dp)
                    .size(25.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 80.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = quote.quote,
                    fontSize = 19.sp,
                    lineHeight = 38.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.Start)
                )

                Text(
                    text = quote.author,
                    fontSize = 18.sp,
                    color = DarkerGrey,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 30.dp)
                )
            }

            Text(
                text = "Quotes.app",
                fontSize = 16.sp,
                lineHeight = 32.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

/** CODE SNIPPET STYLE */
@Composable
fun CodeSnippetStyleQuoteCard(modifier: Modifier,quote: Quote) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background( Color.LightGray)
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 50.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = Color.Black.copy(alpha = 1f),
                    spotColor = Color.Black.copy(alpha = 1f)
                )
            ,
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
//                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 28.dp, start = 20.dp, top = 24.dp)
                ) {
                    CircleDot(color = Color(0xFFFF3B30))
                    Spacer(modifier = Modifier.width(8.dp))
                    CircleDot(color = Color(0xFFFFCC00))
                    Spacer(modifier = Modifier.width(8.dp))
                    CircleDot(color = Color(0xFF4CD964))
                }

                Text(
                    text = quote.quote,
                    color = Color.White,
                    fontSize = 13.sp,
                    lineHeight = 30.sp,
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp)
                )

                Text(
                    text = quote.author,
                    color = Color(0xFF00E0FF),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 15.dp, start = 18.dp, end = 18.dp, bottom = 6.dp)
                )

                Text(
                    text = "Quotes.app",
                    color = Color(0xFFFF48B0),
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun CircleDot(color: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(color = color, shape = CircleShape)
    )
}

/** brat THEME STYLE */
@Composable
fun BratScreen(modifier: Modifier,quote: Quote) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = bratGreen)
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = quote.quote,
            fontSize = 18.sp,
            fontFamily = bratTheme,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}


//@Preview
@Composable
fun LiquidGlassScreen(
    modifier: Modifier,
    quote: Quote,
    color1: Color,
    color2: Color
)
{
//
//    val gradLight = color1.lighten(2f)
//    val gradDark = color2.darken(2f)


    val hazeState = remember { HazeState() }

    var contentHeight by remember { mutableStateOf(400.dp) }
    val density = LocalDensity.current


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val horizontalPadding = screenWidth * 0.04f // 4% of screen width
    val verticalPadding = screenHeight * 0.04f   // 8% of screen height
    val cardHorizontalPadding = screenWidth * 0.06f // 6% of screen width
    val textPadding = screenWidth * 0.04f // 4% of screen width


/** we need to maintain two box where votrh boxes are above the other one and their properties are
 * exactly the same just one box will apply the blur effect other will take care
 * of displaying the text */



//    val col1: Color = Color(0xFF0030CC)
//    val col2: Color = Color(0xFFf093fb)


    /** box with blur effect  */
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .haze(
                hazeState,
                backgroundColor = MaterialTheme.colorScheme.background,
                tint = Color.Black.copy(alpha = .1f),
                blurRadius = 20.dp,
            )
            .shaderBackground(
                MeshGradient(
                    colors = generateGradientColors(
                        color1,
                        color2
                    ).toTypedArray()
                ),
                fallback = {
                    Brush.horizontalGradient(
                        generateGradientColors(
                          color1,
                            color2
                        )
                    )
                }
            )

    )
    {

        Box(
            modifier = modifier
                .padding(
                    horizontal = cardHorizontalPadding,
                    vertical = verticalPadding
                )
                .fillMaxWidth()
                .wrapContentHeight()
                .height(contentHeight)
                .hazeChild(state = hazeState, shape = RoundedCornerShape(20.dp))
        )

    }



    /** box with text  */
    Box(
        modifier = modifier
            .fillMaxWidth()
//            .padding(horizontalPadding)
            .wrapContentHeight()


    )
    {
        Box(
            modifier = Modifier
                .padding(
                    horizontal = cardHorizontalPadding,
                    vertical = verticalPadding
                )
                .wrapContentHeight()
                .heightIn(min = 400.dp)
                .onSizeChanged { size ->
                    contentHeight = with(density) {
                        size.height.toDp()
                    }
                }
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 1f),
                            Color.White.copy(alpha = .3f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )

        )
        {

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(
                        textPadding, end = textPadding,
                        bottom = 40.dp
                    )
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = quote.quote,
                    fontSize = 20.sp,
                    lineHeight = 35.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = quote.author,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 30.dp),
                    fontFamily = FontFamily(Font(R.font.glaciaiindifference_itallic)),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "Quotes.app",
                fontSize = 16.sp,
                lineHeight = 32.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Center
            )

        }
    }
}

/** CARD WITH IMAGE THEME */
@Composable
fun CardImageStyle(modifier: Modifier, quote: Quote, imageModel: Any?) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .wrapContentSize(Alignment.Center)
            .padding(12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 30.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black.copy(alpha = 0.6f),
                    spotColor = Color.Black.copy(alpha = 0.6f)
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = imageModel ?: R.drawable.sample3,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = quote.quote,
                        fontSize = 18.sp,
                        lineHeight = 30.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.glaciaiindifference_regular)),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .align(Alignment.Start),
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = quote.author,
                        fontSize = 14.sp,
                        color = DarkerGrey,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }
}

/** ARTISAN CARD THEME (new) */
@Composable
fun ArtisanCardStyle(modifier: Modifier, quote: Quote, imageModel: Any?) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFE9E9EA)) // light grey background for the screen
            .wrapContentSize(Alignment.Center)
            .padding(12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp, vertical = 30.dp)
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
                    model = imageModel ?: R.drawable.sample3,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )

                // content area with quote and author
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)) {

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

                    // decorative barcode-like box at bottom left
                    Box(
                        modifier = Modifier
                            .height(28.dp)
                            .width(160.dp)
                            .background(Color.Black, shape = RoundedCornerShape(4.dp))
                    ) {}
                }
            }
        }
    }
}

fun generateGradientColors(color1: Color, color2: Color, steps: Int = 6): List<Color> {
    val colors = buildList {
        for (i in 0 until steps) {
            val t = i / (steps - 1).toFloat()
            val interpolatedColor = lerp(color1, color2, t)
            add(interpolatedColor)
        }
    }

    return colors
}

fun lerp(color1: Color, color2: Color, t: Float): Color {
    val r = (color1.red * (1 - t) + color2.red * t).coerceIn(0f, 1f)
    val g = (color1.green * (1 - t) + color2.green * t).coerceIn(0f, 1f)
    val b = (color1.blue * (1 - t) + color2.blue * t).coerceIn(0f, 1f)
    val a = (color1.alpha * (1 - t) + color2.alpha * t).coerceIn(0f, 1f)

    return Color(r, g, b, a)
}

/** IGOR THEME STYLE */
@Composable
fun IgorScreen(modifier: Modifier, quote: Quote) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xfff1aec3))
    ) {
        Image(
            painter = painterResource(id = R.drawable.igor_angels),
            contentDescription = "Cherubs",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-45).dp, y = (-55).dp)
                .size(170.dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.igor_flowers),
            contentDescription = "Sunflowers",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (45).dp, y = (-60).dp)
                .size(170.dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.igor_crocodiles),
            contentDescription = "Crocodile",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-45).dp, y = 50.dp)
                .size(180.dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.igor_statue),
            contentDescription = "Statue Collage",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 55.dp, y = 30.dp)
                .size(230.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = quote.quote,
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Black,
                fontFamily = handWritten,
                fontWeight = FontWeight.Bold
            )
            ,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 15.dp, vertical = 230.dp)
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun ReminderStyle (
    modifier: Modifier = Modifier,
    quote: Quote = Quote(quote = "Pausing for a moment to look to inspiring leaders", author = "Unknown", liked = true),
    bgColor: Color = Color.LightGray,
    textColor: Color = Color(0xFF4B6AD1),
    cardBgColor : Color = Color(0xFFE3EDFD)
) {


        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(.8f)
                .background(bgColor)
                .wrapContentSize(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .wrapContentHeight()
                    .shadow(elevation = 20.dp, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(cardBgColor)

            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 20.dp),
                    text = "Reminder",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = quote.quote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp
                )

                HorizontalDivider()

                Text(
                    text = "Okay",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
}




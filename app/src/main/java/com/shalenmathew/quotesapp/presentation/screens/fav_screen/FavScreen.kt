package com.shalenmathew.quotesapp.presentation.screens.fav_screen

import androidx.compose.animation.core.EaseOut
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.glance.text.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.presentation.screens.fav_screen.util.FavQuoteEvent
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.Grey
import com.shalenmathew.quotesapp.presentation.theme.Orange
import com.shalenmathew.quotesapp.presentation.theme.PurpleGrey40
import com.shalenmathew.quotesapp.presentation.theme.Red
import com.shalenmathew.quotesapp.presentation.viewmodel.FavQuoteViewModel


@Composable
fun FavScreen(paddingValues: PaddingValues,
              navHost: NavHostController,
              quoteViewModel:FavQuoteViewModel= hiltViewModel()) {

    val state = quoteViewModel.favQuoteState.value

    var clickedSearch by remember {
        mutableStateOf(false)
    }

    val progress by animateFloatAsState(targetValue = if(clickedSearch) 1f else 0f, label = "", animationSpec = tween(2000))

    Box(modifier=Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .background(color = Color.Black)){

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent), contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent)
                        , contentAlignment = Alignment.Center){
                        Text(state.error, color = White)
                    }
                }
            }

        Column(modifier=Modifier.fillMaxSize()) {


            OutlinedTextField(
                value = state.query,
                onValueChange = { value ->
                    quoteViewModel.onEvent(FavQuoteEvent.onSearchQueryChanged(value))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 15.dp,
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp
                    )
                    .onFocusChanged { focusState ->
                        clickedSearch = focusState.isFocused
                    }
                    .animatedBorder({ progress }, White, Color.Black),
                maxLines = 1,
                shape = MaterialTheme.shapes.extraLarge,
                placeholder = { Text(text = "Search...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Grey,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    disabledPlaceholderColor = Color.Yellow,
                    focusedTextColor = White,
                )
            )


            if(state.dataList.isNotEmpty()){

                LazyColumn(modifier=Modifier.fillMaxSize()) {
                    items(state.dataList) { quote ->
                        FavQuoteItem(quote, quoteViewModel,navHost)

                    }
                }


            }else{

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Looks empty...",
                        color = White,
                        fontFamily = GIFont,
                    )
                }
            }


        }
        }

    }


fun Modifier.animatedBorder
            (provideProgress: () -> Float,
             colorFocused: Color,
             colorUnfocused: Color) = this.drawWithCache {
    val width = size.width
    val height = size.height

    val shape = CircleShape

    // Only works with RoundedCornerShape...
    val outline = shape.createOutline(size, layoutDirection, this) as Outline.Rounded

    // ... correction: Only works with same corner sizes everywhere
    val radius = outline.roundRect.topLeftCornerRadius.x
    val diameter = 2 * radius

    // Clockwise path
    val pathCw = Path()

    // Start top center
    pathCw.moveTo(width / 2, 0f)

    // Line to right
    pathCw.lineTo(width - radius, 0f)

    // Top right corner
    pathCw.arcTo(Rect(width - diameter, 0f, width, diameter), -90f, 90f, false)

    // Right edge
    pathCw.lineTo(width, height - radius)

    // Bottom right corner
    pathCw.arcTo(Rect(width - diameter, height - diameter, width, height), 0f, 90f, false)

    // Line to bottom center
    pathCw.lineTo(width / 2, height)

    // As above, but mirrored horizontally
    val pathCcw = Path()
    pathCcw.moveTo(width / 2, 0f)
    pathCcw.lineTo(radius, 0f)
    pathCcw.arcTo(Rect(0f, 0f, diameter, diameter), -90f, -90f, false)
    pathCcw.lineTo(0f, height - radius)
    pathCcw.arcTo(Rect(0f, height - diameter, diameter, height), 180f, -90f, false)
    pathCcw.lineTo(width / 2, height)

    val pmCw = PathMeasure().apply {
        setPath(pathCw, false)
    }
    val pmCcw = PathMeasure().apply {
        setPath(pathCcw, false)
    }

    fun DrawScope.drawIndicator(progress: Float, pathMeasure: PathMeasure) {
        val subPath = Path()
        pathMeasure.getSegment(0f, pathMeasure.length * EaseOut.transform(progress), subPath)
        drawPath(subPath, colorFocused, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
    }

    onDrawBehind {
        // Draw the shape
        drawOutline(outline, colorUnfocused, style = Stroke(2.dp.toPx()))

        // Draw the indicators
        drawIndicator(provideProgress(), pmCw)
        drawIndicator(provideProgress(), pmCcw)
    }
}

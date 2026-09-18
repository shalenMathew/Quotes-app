package com.shalenmathew.quotesapp.presentation.screens.library.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customBlack
import com.shalenmathew.quotesapp.presentation.theme.customGrey

@Composable
fun LibraryQuoteItem(
    quote: Quote,
    showHeart: Boolean = false,
    showDelete: Boolean = false,
    showEdit: Boolean = false,
    onLikeClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val gradient = Brush.radialGradient(
        0.0f to customBlack,
        1.0f to customGrey,
        radius = 600.0f,
        tileMode = TileMode.Repeated
    )

    Box(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            AsyncImage(
                model = R.drawable.quotation,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp, top = 10.dp)
                    .size(30.dp)
            )

            Text(
                text = quote.quote,
                fontFamily = GIFont,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                color = Color.White,
                style = TextStyle(lineHeight = 40.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = quote.author,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 15.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = R.drawable.send,
                    contentDescription = "Share",
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { onShareClick() }
                )

                if (showEdit) {
                    Spacer(modifier = Modifier.width(12.dp))
                    AsyncImage(
                        model = R.drawable.edit_icon,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { onEditClick() }
                    )
                }

                if (showHeart) {
                    Spacer(modifier = Modifier.width(15.dp))
                    AsyncImage(
                        model = if (quote.liked) R.drawable.heart_filled else R.drawable.heart_unfilled,
                        contentDescription = "Like",
                        modifier = Modifier
                            .size(35.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onLikeClick()
                            }
                    )
                }

                if (showDelete) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp)
                            .clickable { onDeleteClick() }
                    )
                }
            }
        }
    }
}

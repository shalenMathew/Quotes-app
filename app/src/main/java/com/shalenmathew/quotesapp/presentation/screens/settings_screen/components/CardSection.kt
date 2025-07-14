package com.shalenmathew.quotesapp.presentation.screens.settings_screen.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import androidx.core.net.toUri
import com.shalenmathew.quotesapp.presentation.theme.customGrey
import com.shalenmathew.quotesapp.presentation.theme.customGrey2

@Composable
fun CardSection(index: Int) {

    val context = LocalContext.current
    val card = cardsRow[index]
    val isFirst = index == 0
    val isLast = index == cardsRow.size - 1

    val shape = when {
        isFirst -> RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
        isLast -> RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
        else -> RoundedCornerShape(0.dp)
    }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.6.dp,
                    color = Color.Black,
                    shape = RectangleShape )
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, card.url.toUri())
                    context.startActivity(intent)
                }
                .clip(shape)
//                .background(Color(0xFF1C1C1E))
                .background(customGrey2)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(card.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(30.dp)

                )

                Text(
                    text = card.name,
                    color = Color.White,
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            androidx.compose.material3.Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White
            )
    }

}



val cardsRow = listOf<CardRow>(
    CardRow(icon = R.drawable.ic_twitter, name = "Twitter", url = "https://x.com/shalenmathew" ),
    CardRow(icon = R.drawable.ic_github, name = "Github", url = "https://github.com/shalenMathew/Quotes-app" ),
    CardRow(icon = R.drawable.ic_discord, name = "Discord", url = "https://discord.gg/QpDJh3rT4q" ),
    CardRow(icon = R.drawable.ic_linkedin, name = "LinkedIn", url = "https://www.linkedin.com/in/shalen-mathew-3b566921b" ),
    CardRow(icon = R.drawable.ic_link, name = "LinkTree", url = "https://linktr.ee/shalenmathew"),
    CardRow(icon = R.drawable.ic_coffee, name = "buy me a coffee", url = "https://buymeacoffee.com/shalenmathew"),
    CardRow(icon = R.drawable.ic_coffee_beans, name = "ko-fi", url = "https://ko-fi.com/shalenmathew"),
)

data class CardRow(val icon: Int,val name: String, val url: String)
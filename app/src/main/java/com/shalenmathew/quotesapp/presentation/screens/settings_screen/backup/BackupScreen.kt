package com.shalenmathew.quotesapp.presentation.screens.settings_screen.backup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customGrey2

@Composable
fun BackupScreen(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Backup & Restore",
            fontFamily = GIFont,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Export your liked and custom quotes to a backup file or import them back.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        BackupActionCard(
            title = "Export Data",
            description = "Save your data as a backup file",
            icon = R.drawable.upload,
            onClick = { /* TODO: Implement Export */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BackupActionCard(
            title = "Import Data",
            description = "Restore data from a backup file",
            icon = R.drawable.downloads,
            onClick = { /* TODO: Implement Import */ }
        )
    }
}

@Composable
fun BackupActionCard(
    title: String,
    description: String,
    icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(customGrey2)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = GIFont
            )
            Text(
                text = description,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

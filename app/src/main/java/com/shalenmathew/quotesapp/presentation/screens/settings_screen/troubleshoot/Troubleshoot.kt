package com.shalenmathew.quotesapp.presentation.screens.settings_screen.troubleshoot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.troubleshoot.component.TroubleshootItem
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.troubleshoot.component.troubleshootCategories
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customGrey2

@Composable
fun Troubleshoot(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Troubleshoot",
            fontFamily = GIFont,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            troubleshootCategories.forEach { category ->
                item {
                    Text(
                        text = category.title,
                        fontFamily = GIFont,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }
                items(category.items) { item ->
                    ExpandableCard(item)
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(item: TroubleshootItem) {
    var expanded by remember { mutableStateOf(false) }

    // Animates the rotation of the arrow
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = customGrey2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.question,
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Drop Down Arrow",
                    tint = Color.White,
                    modifier = Modifier.rotate(rotationState)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = item.answer,
                    modifier = Modifier.padding(top = 12.dp),
                    color = Color.LightGray,
                    fontFamily = GIFont,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
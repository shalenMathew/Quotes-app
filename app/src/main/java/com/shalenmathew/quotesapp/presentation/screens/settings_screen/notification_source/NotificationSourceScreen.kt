package com.shalenmathew.quotesapp.presentation.screens.settings_screen.notification_source

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.viewmodel.SettingsViewModel
import com.shalenmathew.quotesapp.util.getNotificationSources
import kotlinx.coroutines.flow.first

@Composable
fun NotificationSourceScreen(
    paddingValues: PaddingValues,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedSources by remember { mutableStateOf(setOf("network")) }

    LaunchedEffect(Unit) {
        selectedSources = context.getNotificationSources().first()
    }

    val options = listOf(
        NotificationSourceOption("Fav Quotes", "favourite", "Fetched from your favorites"),
        NotificationSourceOption("Custom Quotes", "custom", "Fetched from your own creations"),
        NotificationSourceOption("Network", "network", "Fetched fresh from the internet")
    )

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Notification Content Source",
            fontFamily = GIFont,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Choose which quotes to be shown in notifications. Select one or more.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column {
            options.forEach { option ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            val newSources = selectedSources.toMutableSet()
                            if (newSources.contains(option.value)) {
                                if (newSources.size > 1) {
                                    newSources.remove(option.value)
                                }
                            } else {
                                newSources.add(option.value)
                            }
                            selectedSources = newSources
                            settingsViewModel.saveNotificationSources(newSources)
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedSources.contains(option.value),
                        onCheckedChange = null,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.Black
                        )
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = option.label,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = option.description,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Text(
            text = "(Notifications might not appear if this app is battery optimized. Please make sure you're not keeping the app in battery saver mode for all features to work properly.)",
            color = Color.Gray,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

data class NotificationSourceOption(
    val label: String,
    val value: String,
    val description: String
)

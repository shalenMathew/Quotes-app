package com.shalenmathew.quotesapp.presentation.screens.settings_screen.widget_source

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.viewmodel.SettingsViewModel
import com.shalenmathew.quotesapp.util.getWidgetSource
import com.shalenmathew.quotesapp.util.setWidgetSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun WidgetSourceScreen(
    paddingValues: PaddingValues,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedSource by remember { mutableStateOf("favorites") }

    LaunchedEffect(Unit) {
        selectedSource = context.getWidgetSource().first()
    }

    val options = listOf(
        WidgetSourceOption("Fav Quotes", "favorites", "Fetched from your favorites"),
        WidgetSourceOption("Custom Quotes", "custom", "Fetched from your own creations"),
        WidgetSourceOption("Network", "network", "Fetched fresh from the internet")
    )

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Widget Content Source",
            fontFamily = GIFont,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Choose which quotes to be displayed on widget.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(Modifier.selectableGroup()) {
            options.forEach { option ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (selectedSource == option.value),
                            onClick = {
                                selectedSource = option.value
                                scope.launch {
                                    context.setWidgetSource(option.value)
                                    settingsViewModel.scheduleWidgetRefreshWorkManager()
                                }
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedSource == option.value),
                        onClick = null, // null recommended for accessibility with selectable
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = Color.Gray
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
            text = "(The widget might not refresh if this app is battery optimized. Please make sure you're not keeping the app in battery saver mode for all features to work properly.)",
            color = Color.Gray,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

data class WidgetSourceOption(
    val label: String,
    val value: String,
    val description: String
)

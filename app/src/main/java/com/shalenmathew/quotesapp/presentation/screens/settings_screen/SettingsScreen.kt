package com.shalenmathew.quotesapp.presentation.screens.settings_screen

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.BuildConfig
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.components.CardSection
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.components.cardsRow
import com.shalenmathew.quotesapp.presentation.theme.*
import com.shalenmathew.quotesapp.presentation.viewmodel.SettingsViewModel
import com.shalenmathew.quotesapp.presentation.widget.QuotesWidgetReceiver
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_WIDGET_REFRESH_INTERVAL
import com.shalenmathew.quotesapp.util.getMillisFromNow
import com.shalenmathew.quotesapp.util.getWidgetRefreshInterval
import com.shalenmathew.quotesapp.util.setWidgetRefreshInterval
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Widget options
val timeOptions = listOf("1 hr", "6 hr", "12 hr", "24 hr")

// Notification options
val notificationOptions = listOf(
    "Daily",
    "Every 2 Days",
    "Weekly"
)

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    var isQuotesWidgetEnabled by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isQuotesWidgetEnabled = isQuotesWidgetEnabled(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = "Settings",
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(15.dp),
                color = Color.White,
                fontSize = 35.sp
            )

            LazyColumn {

                item {
                    Text(
                        text = "Socials",
                        color = Color.White,
                        modifier = Modifier.padding(start = 12.dp, bottom = 12.dp),
                        fontSize = 20.sp,
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )
                }

                items(cardsRow.size) { index ->
                    CardSection(index = index, navHost = navHost)
                }

                item {
                    Text(
                        text = "Widget Settings",
                        color = Color.White,
                        modifier = Modifier.padding(15.dp),
                        fontSize = 20.sp,
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Medium
                    )

                    // Widget Refresh Time
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.6.dp, Color.Black, RectangleShape)
                            .clip(RoundedCornerShape(12.dp))
                            .background(customGrey2)
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_widgets),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )

                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp),
                            text = "Widget Refresh Time",
                            color = Color.White,
                            fontFamily = GIFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )

                        TimeDropdownMenu(
                            modifier = Modifier.widthIn(max = 120.dp),
                            isEnable = isQuotesWidgetEnabled,
                            onTimeSelected = {
                                val interval = it.removeSuffix("hr").trim().toInt()
                                coroutineScope.launch {
                                    context.setWidgetRefreshInterval(interval)
                                }
                                settingsViewModel.scheduleWidgetRefreshWorkAlarm(
                                    getMillisFromNow(interval)
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notification Frequency
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.6.dp, Color.Black, RectangleShape)
                            .clip(RoundedCornerShape(12.dp))
                            .background(customGrey2)
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_widgets),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )

                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp),
                            text = "Notification Frequency",
                            color = Color.White,
                            fontFamily = GIFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )

                        NotificationDropdownMenu(
                            modifier = Modifier.widthIn(max = 140.dp),
                            onOptionSelected = {
                                settingsViewModel.updateNotificationFrequency(it)
                            }
                        )
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (BuildConfig.DEBUG)
                                "made with ❤ by Shalen Mathew (debug.mode)"
                            else
                                "made with ❤ by Shalen Mathew",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDropdownMenu(
    modifier: Modifier = Modifier,
    onTimeSelected: (String) -> Unit,
    isEnable: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("-") }

    LaunchedEffect(Unit) {
        selectedOption = "${getSelectedWidgetRefreshInterval(context)} hr"
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.height(50.dp)
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = customGrey3,
                unfocusedContainerColor = customGrey3
            ),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium,
                fontFamily = GIFont,
                color = Color.White,
                fontSize = 16.sp
            ),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(customGrey4)
        ) {
            timeOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        if (!isEnable) {
                            Toast.makeText(context, "No widgets detected", Toast.LENGTH_SHORT).show()
                        } else {
                            selectedOption = option
                            expanded = false
                            onTimeSelected(option)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDropdownMenu(
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Every 2 Days") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.height(50.dp)
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = customGrey3,
                unfocusedContainerColor = customGrey3
            ),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium,
                fontFamily = GIFont,
                color = Color.White,
                fontSize = 16.sp
            ),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(customGrey4)
        ) {
            notificationOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

private suspend fun getSelectedWidgetRefreshInterval(context: Context): Int {
    var selectedInterval =
        context.getWidgetRefreshInterval().first() ?: DEFAULT_WIDGET_REFRESH_INTERVAL
    if (!timeOptions.contains("$selectedInterval hr")) {
        selectedInterval = DEFAULT_WIDGET_REFRESH_INTERVAL
    }
    return selectedInterval
}

fun isQuotesWidgetEnabled(context: Context): Boolean {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val componentName = ComponentName(context, QuotesWidgetReceiver::class.java)
    return appWidgetManager.getAppWidgetIds(componentName).isNotEmpty()
}

package com.shalenmathew.quotesapp.presentation.screens.settings_screen

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.Poppins
import com.shalenmathew.quotesapp.presentation.theme.customGrey2
import com.shalenmathew.quotesapp.presentation.theme.customGrey3
import com.shalenmathew.quotesapp.presentation.theme.customGrey4
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

// Only Hr is allowed
val timeOptions = listOf("1 hr", "6 hr", "12 hr", "24 hr")

@Composable
fun SettingsScreen(paddingValues: PaddingValues, navHost: NavHostController , settingsViewModel : SettingsViewModel = hiltViewModel()) {

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
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
    )
    {

        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        )
        {


            Text(
                text = "Settings", fontFamily = GIFont, fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
                color = Color.White,
                fontSize = 35.sp
            )


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                item {
                    Text(
                        text = "Socials", color = Color.White,
                        modifier = Modifier.padding(start = 12.dp, bottom = 12.dp),
                        fontSize = 20.sp,
                        fontFamily = GIFont, fontWeight = FontWeight.Medium
                    )
                }

                items(cardsRow.size) { cardRowIndex ->
                    CardSection(index = cardRowIndex, navHost = navHost)
                }

                item {
                    Text(
                        text = "Widget Settings", color = Color.White,
                        modifier = Modifier.padding(15.dp),
                        fontSize = 20.sp,
                        fontFamily = GIFont, fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 0.6.dp,
                                color = Color.Black,
                                shape = RectangleShape
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(customGrey2)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_widgets),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(30.dp)
                        )
                        Text(modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                            text = "Widget Refresh Time",
                            color = Color.White,
                            fontFamily = GIFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        TimeDropdownMenu(modifier = Modifier.widthIn(max = 120.dp), onTimeSelected = {
                            val interval = it.removeSuffix("hr").trim().toInt()
                            coroutineScope.launch {
                                context.setWidgetRefreshInterval(interval = interval)
                            }
                            settingsViewModel.scheduleWidgetRefreshWorkAlarm(getMillisFromNow(interval))
                        },isEnable = isQuotesWidgetEnabled)
                    }

                }

            }

            Spacer(Modifier.weight(1f))

            if (BuildConfig.DEBUG) {

                Box(modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center)
                {
                    Text(
//                        text = "made with \uD83E\uDD75 by Shalen Mathew (debug.mode)",
                        text = "made with ❤\uFE0F by Shalen Mathew (debug.mode)",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium
                    )

                }

            } else {

                Box(modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center)
                {
                    Text(
//                        text = "made with \uD83E\uDD75 by Shalen Mathew",
                        text = "made with ❤\uFE0F by Shalen Mathew",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDropdownMenu(
    modifier: Modifier = Modifier,
    onTimeSelected: (String) -> Unit = {},
    isEnable: Boolean,
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
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = customGrey3,
                unfocusedContainerColor = customGrey3
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium,
                fontFamily = GIFont,
                color = Color.White,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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
                            Toast.makeText(context,"Not Widgets Detected" , Toast.LENGTH_SHORT).show()
                        } else {
                            selectedOption = option
                            expanded = false
                            onTimeSelected(option)
                        }
                    },
                    colors = MenuItemColors(
                        textColor = Color.White,
                        leadingIconColor = Color.White,
                        trailingIconColor = Color.White,
                        disabledTextColor = customGrey3,
                        disabledLeadingIconColor = customGrey3,
                        disabledTrailingIconColor = customGrey3,
                    ),
                )
            }
        }
    }
}

private suspend fun getSelectedWidgetRefreshInterval(context: Context): Int {
    var selectedInterval = context.getWidgetRefreshInterval().first()?:DEFAULT_WIDGET_REFRESH_INTERVAL
    if(!timeOptions.contains("$selectedInterval hr")){
        selectedInterval = DEFAULT_WIDGET_REFRESH_INTERVAL
    }
    return selectedInterval
}

fun isQuotesWidgetEnabled(context: Context): Boolean {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val componentName = ComponentName(context, QuotesWidgetReceiver::class.java)
    val widgetIds = appWidgetManager.getAppWidgetIds(componentName)
    return widgetIds.isNotEmpty()
}

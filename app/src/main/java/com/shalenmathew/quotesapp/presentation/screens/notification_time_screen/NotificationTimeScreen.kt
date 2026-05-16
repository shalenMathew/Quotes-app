package com.shalenmathew.quotesapp.presentation.screens.notification_time_screen

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.RefreshType
import com.shalenmathew.quotesapp.presentation.screens.settings_screen.TimeDropdownMenu
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.bratGreen
import com.shalenmathew.quotesapp.presentation.theme.customGrey2
import com.shalenmathew.quotesapp.presentation.viewmodel.SettingsViewModel
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_DAILY_NOTIFICATION_HOUR
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_DAILY_NOTIFICATION_MINUTE
import com.shalenmathew.quotesapp.util.NotificationMode
import com.shalenmathew.quotesapp.util.convertDaysToHrs
import com.shalenmathew.quotesapp.util.formatDailyTime
import com.shalenmathew.quotesapp.util.getNotificationDailyTime
import com.shalenmathew.quotesapp.util.getNotificationMode
import kotlinx.coroutines.flow.first

@Composable
fun NotificationTimeScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var mode by rememberSaveable { mutableStateOf(NotificationMode.FREQUENCY) }
    var dailyHour by rememberSaveable { mutableIntStateOf(DEFAULT_DAILY_NOTIFICATION_HOUR) }
    var dailyMinute by rememberSaveable { mutableIntStateOf(DEFAULT_DAILY_NOTIFICATION_MINUTE) }
    var hasDailyTime by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mode = context.getNotificationMode().first()
        context.getNotificationDailyTime().first()?.let { (h, m) ->
            dailyHour = h
            dailyMinute = m
            hasDailyTime = true
        }
    }

    val frequencyActive = mode == NotificationMode.FREQUENCY
    val dailyActive = mode == NotificationMode.DAILY_TIME

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navHost.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Notification Time",
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .alpha(if (frequencyActive) 1f else 0.5f)
                .clip(RoundedCornerShape(12.dp))
                .background(customGrey2)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActiveIndicator(active = frequencyActive)
            Image(
                painter = painterResource(R.drawable.ic_notifications),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(30.dp)
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                text = "Notify me every",
                color = Color.White,
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            TimeDropdownMenu(
                modifier = Modifier.widthIn(max = 120.dp),
                onTimeSelected = {
                    val interval = if (it.contains("days")) {
                        convertDaysToHrs(it.removeSuffix("days").trim().toInt())
                    } else {
                        it.removeSuffix("hr").trim().toInt()
                    }
                    mode = NotificationMode.FREQUENCY
                    settingsViewModel.applyFrequencyMode(interval)
                },
                isEnable = true,
                refreshType = RefreshType.NOTIFICATION
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "or",
                color = Color.White,
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .alpha(if (dailyActive) 1f else 0.5f)
                .clip(RoundedCornerShape(12.dp))
                .background(customGrey2)
                .clickable {
                    TimePickerDialog(
                        context,
                        { _, h, m ->
                            dailyHour = h
                            dailyMinute = m
                            hasDailyTime = true
                            mode = NotificationMode.DAILY_TIME
                            settingsViewModel.applyDailyMode(h, m)
                        },
                        dailyHour,
                        dailyMinute,
                        false
                    ).show()
                }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActiveIndicator(active = dailyActive)
            Image(
                painter = painterResource(R.drawable.ic_notifications),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(30.dp)
            )
            Text(
                modifier = Modifier.weight(1f),
                text = if (hasDailyTime) {
                    "Notify everyday at : ${formatDailyTime(dailyHour, dailyMinute)}"
                } else {
                    "Pick a time to be notified everyday"
                },
                color = Color.White,
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ActiveIndicator(active: Boolean) {
    Box(
        modifier = Modifier
            .padding(end = 10.dp)
            .size(8.dp)
            .clip(CircleShape)
            .background(if (active) bratGreen else Color.Transparent)
    )
}

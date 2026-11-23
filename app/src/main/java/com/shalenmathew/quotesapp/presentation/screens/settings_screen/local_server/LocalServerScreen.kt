package com.shalenmathew.quotesapp.presentation.screens.settings_screen.local_server

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customGrey2
import com.shalenmathew.quotesapp.presentation.viewmodel.LocalServerViewModel

@Composable
fun LocalServerScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    viewModel: LocalServerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val status by viewModel.serverStatus.collectAsState()
    val customCount by viewModel.customQuotesCount.collectAsState(initial = 0)
    val likedCount by viewModel.likedQuotesCount.collectAsState(initial = 0)

    LaunchedEffect(Unit) {
        viewModel.refreshNetworkAddress()
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
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
                text = "Local Web Manager",
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Text(
            text = "Manage, batch upload, and sync quotes from any PC browser connected to the same Wi-Fi router.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Toggle Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(customGrey2)
                .clickable { viewModel.toggleServer(!status.isRunning) }
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (status.isRunning) Color(0xFF22C55E) else Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (status.isRunning) "Server Running" else "Server Stopped",
                        color = Color.White,
                        fontFamily = GIFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (status.isRunning) "Accessible on your local Wi-Fi subnet"
                    else "Turn on to start local HTTP server",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Switch(
                checked = status.isRunning,
                onCheckedChange = { viewModel.toggleServer(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFA855F7),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // URL Display Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(customGrey2)
                .border(
                    width = 1.dp,
                    color = if (status.isRunning) Color(0xFFA855F7) else Color.Transparent,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PC Browser Address (LAN URL)",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontFamily = GIFont
                )
                IconButton(
                    onClick = { viewModel.refreshNetworkAddress() },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh IP",
                        tint = Color.Gray
                    )
                }
            }

            val displayUrl = if (status.isRunning) status.url else "http://${status.ipAddress}:${status.port}"

            Text(
                text = displayUrl,
                color = if (status.isRunning) Color.White else Color.Gray,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            status.errorMessage?.let { err ->
                Text(
                    text = err,
                    color = Color(0xFFEF4444),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Quotes LAN URL", displayUrl))
                    Toast.makeText(context, "Copied $displayUrl", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Copy URL",
                    fontFamily = GIFont,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Database Sync Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(customGrey2)
                    .padding(16.dp)
            ) {
                Text(text = "Custom Quotes", color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = "$customCount",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = GIFont
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(customGrey2)
                    .padding(16.dp)
            ) {
                Text(text = "Favorite Quotes", color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = "$likedCount",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = GIFont
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Instructions card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(customGrey2)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "How to use",
                color = Color.White,
                fontFamily = GIFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "1. Connect your phone and PC to the same Wi-Fi network.\n" +
                    "2. Turn on the server above and enter the LAN URL in your PC browser.\n" +
                    "3. Batch paste quotes, upload CSV/JSON files, edit or organize collections—all changes sync immediately to your phone database and home screen widget.",
                color = Color.Gray,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

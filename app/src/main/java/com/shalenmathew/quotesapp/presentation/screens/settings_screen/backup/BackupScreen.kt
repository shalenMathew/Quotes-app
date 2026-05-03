package com.shalenmathew.quotesapp.presentation.screens.settings_screen.backup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shalenmathew.quotesapp.R
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customGrey2
import com.shalenmathew.quotesapp.presentation.viewmodel.BackupDataViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BackupScreen(
    paddingValues: PaddingValues
) {

    val context = LocalContext.current
    val viewModel: BackupDataViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()


    val exportLauncher = rememberLauncherForActivityResult(

        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri->

            uri?.let {
                viewModel.exportData(context = context, uri = uri){success ->
                    val msg = if (success) "Export Successful" else "Export Failed"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                }
            }

        }
    )


    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                viewModel.importData(context, it) { success ->
                    val msg = if (success) "Import Successful" else "Import Failed"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

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
            title = if (viewModel.isLoading) "Processing..." else "Export Data",
            description = "Save your data as a backup file",
            icon = R.drawable.upload,
            isLoading = viewModel.isLoading,
            enabled = !viewModel.isLoading,
            onClick = {
                scope.launch {
                    if (viewModel.getLikedCount() > 0 || viewModel.getCustomCount() > 0) {
                        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                        val fileName = "QuotesApp_Backup_$timeStamp.json"
                        exportLauncher.launch(fileName)
                    } else {
                        Toast.makeText(context, "No quotes found to backup!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BackupActionCard(
            title = if (viewModel.isLoading) "Processing..." else "Import Data",
            description = "Restore data from a backup file",
            icon = R.drawable.downloads,
            isLoading = viewModel.isLoading,
            enabled = !viewModel.isLoading,
            onClick = { importLauncher.launch(arrayOf("application/json", "application/octet-stream")) }
        )
    }
}

@Composable
fun BackupActionCard(
    title: String,
    description: String,
    icon: Int,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(customGrey2)
            .clickable(enabled = enabled) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
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

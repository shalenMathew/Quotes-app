package com.shalenmathew.quotesapp.presentation.screens.settings_screen.zen_audio

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customGrey2
import com.shalenmathew.quotesapp.presentation.viewmodel.SettingsViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun ZenAudioSettingsScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isZenAudioEnabled by viewModel.isZenAudioEnabled.collectAsState(initial = false)
    val customAudioPath by viewModel.zenAudioCustomPath.collectAsState(initial = null)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val path = copyAudioToInternalStorage(context, it)
            if (path != null) {
                viewModel.setZenAudioCustomPath(path)
                Toast.makeText(context, "Audio updated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to load audio", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                .padding(vertical = 15.dp, horizontal = 0.dp),
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
                text = "Zen Audio Settings",
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Master Switch
        SettingsRow(
            title = "Enable Zen Audio",
            subtitle = "Play background audio while using the app",
            trailingContent = {
                Switch(
                    checked = isZenAudioEnabled,
                    onCheckedChange = { viewModel.toggleZenAudio(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Gray,
                        uncheckedThumbColor = Color.DarkGray,
                        uncheckedTrackColor = Color.Black
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Audio Source",
            color = Color.White,
            fontFamily = GIFont,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Default Audio Indicator
        SettingsRow(
            title = "Default Audio",
            subtitle = "Water Flow sound",
            isSelected = customAudioPath == null,
            onClick = {
                viewModel.setZenAudioCustomPath(null)
                deleteCustomAudio(context)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Custom Audio Picker
        SettingsRow(
            title = if (customAudioPath != null) "Custom Audio" else "Select Custom Audio",
            subtitle = customAudioPath?.let { getFileNameFromPath(it) } ?: "Pick an audio file from your device",
            isSelected = customAudioPath != null,
            onClick = { launcher.launch("audio/*") },
            trailingContent = if (customAudioPath != null) {
                {
                    IconButton(onClick = {
                        viewModel.setZenAudioCustomPath(null)
                        deleteCustomAudio(context)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = Color.Red
                        )
                    }
                }
            } else null
        )
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(customGrey2)
            .then(
                if (isSelected) Modifier.border(1.dp, Color.White, RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontFamily = GIFont,
                fontSize = 12.sp
            )
        }
        trailingContent?.invoke()
    }
}

private fun copyAudioToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val originalFileName = getFileNameFromUri(context, uri) ?: "custom_audio.mp3"
        val fileName = "custom_zen_audio_${System.currentTimeMillis()}_$originalFileName"
        val file = File(context.filesDir, fileName)

        // Clean up old custom files first
        deleteCustomAudio(context)

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = it.getString(index)
                }
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != null && cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}

private fun deleteCustomAudio(context: Context) {
    val files = context.filesDir.listFiles { _, name -> name.startsWith("custom_zen_audio_") }
    files?.forEach { it.delete() }
}

private fun getFileNameFromPath(path: String): String {
    val name = File(path).name
    return if (name.startsWith("custom_zen_audio_")) {
        // name format: custom_zen_audio_TIMESTAMP_ORIGINALNAME
        val parts = name.split("_")
        if (parts.size >= 5) {
            parts.drop(4).joinToString("_")
        } else {
            name.substringAfterLast("_")
        }
    } else {
        name
    }
}

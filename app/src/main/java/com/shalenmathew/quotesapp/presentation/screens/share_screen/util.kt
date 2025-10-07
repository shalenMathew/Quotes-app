package com.shalenmathew.quotesapp.presentation.screens.share_screen

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import java.io.File

/**  this util scope  limited to share screen */
fun saveImgInGallery(context: Context , bitmap: Bitmap) {

    val fileName = "quote_${System.currentTimeMillis()}.png"


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

        val contentValues  = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Quotes")
        }

        val imageUri  = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)

        imageUri?.let { uri->
            context.contentResolver.openOutputStream(uri)?.use { stream ->

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

                Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
            }

        }?:run {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }

    }
    else{

        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val quotesFolder = File(picturesDir, "Quotes")

        if (!quotesFolder.exists()) quotesFolder.mkdirs()

        val imageFile = File(quotesFolder, fileName)
        try {
            imageFile.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            MediaScannerConnection.scanFile(
                context,
                arrayOf(imageFile.absolutePath),
                arrayOf("image/png"),
                null
            )

            Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("SaveError", "Failed to save image: ${e.message}")
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }

    }

}

fun shareImg(context: Context , bitmap: Bitmap){

    val file = File(context.cacheDir, "shared_image.png")
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent,"Share Quotes via"))


}

sealed class QuoteStyle()
{

    object DefaultTheme : QuoteStyle()
    object CodeSnippetTheme : QuoteStyle()

    object LiquidGlassTheme : QuoteStyle()
//    object SpotifyTheme: QuoteStyle()
    object bratTheme : QuoteStyle()
    object igorTheme : QuoteStyle()

    object ReminderTheme : QuoteStyle()

}

@Composable
fun CustomPickerDialog(
    initialColor: Color,
    onSelect: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val controller = rememberColorPickerController()

    CustomPickerDialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            HsvColorPicker(
                modifier = Modifier
                    .width(350.dp)
                    .height(300.dp)
                    .padding(top = 10.dp),
                initialColor = initialColor,
                controller = controller
            )

            BrightnessSlider(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(35.dp),
                initialColor = initialColor,
                controller = controller
            )

            AlphaTile(
                modifier = Modifier
                    .size(80.dp)
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(6.dp)),
                controller = controller
            )

            Button(
                onClick = {
                    onSelect(controller.selectedColor.value)
                    onDismiss()
                }
            ) {
                Text(
                    text = "Done",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun CustomPickerDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier.widthIn(max = 500.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) { content() }
    }
}


fun Color.lighten(factor: Float = 1.3f): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(this.toArgb(), hsv)
    hsv[2] = (hsv[2] * factor).coerceIn(0f, 1f)  // Increase brightness
    return Color(android.graphics.Color.HSVToColor(hsv))
}

fun Color.darken(factor: Float = 0.7f): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(this.toArgb(), hsv)
    hsv[2] = (hsv[2] * factor).coerceIn(0f, 1f)  // Decrease brightness
    return Color(android.graphics.Color.HSVToColor(hsv))
}
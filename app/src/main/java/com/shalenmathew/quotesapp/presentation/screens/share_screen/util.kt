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
import androidx.core.content.FileProvider
import java.io.File

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

sealed class QuoteStyle(){

    object DefaultTheme : QuoteStyle()
    object CodeSnippetTheme : QuoteStyle()
//    object SpotifyTheme: QuoteStyle()
    object bratTheme : QuoteStyle()
    object igorTheme : QuoteStyle()

}
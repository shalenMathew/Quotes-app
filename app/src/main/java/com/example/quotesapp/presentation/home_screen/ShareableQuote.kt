package com.example.quotesapp.presentation.home_screen

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.quotesapp.R
import com.example.quotesapp.domain.model.Quote
import java.io.File

fun createImageFromXml(context: Context, quote: Quote, callback: (Bitmap) -> Unit) {
    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.share_image_2, null, false)

    // Set the quote text dynamically
    val quoteTextView = view.findViewById<TextView>(R.id.quote_text)
    val authorTextView = view.findViewById<TextView>(R.id.quote_author)

    quoteTextView.text = quote.quote
    authorTextView.text = "${quote.author}"

    // Measure and layout the view

    val width = 1000 // Fixed width
    view.measure(
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )

    val height = view.measuredHeight
    view.layout(0, 0, width,height)

    // Create a Bitmap
    val bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)

    callback(bitmap)
}

fun saveAndShareImage(context: Context, bitmap: Bitmap) {
    val file = File(context.cacheDir, "quote_image.png")
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Share Quote via"))
}

fun showSharePreview(context: Context, bitmap: Bitmap) {

    val view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_box, null)

    val share = view.findViewById<ImageView>(R.id.cd_share)
    val download = view.findViewById<ImageView>(R.id.cd_download)
    val custom = view.findViewById<ImageView>(R.id.cd_customize)
    val cancel = view.findViewById<ImageView>(R.id.cd_cancel)
    val preview = view.findViewById<ImageView>(R.id.cd_preview)

preview.setImageBitmap(bitmap)

    val dialog = AlertDialog.Builder(context)
        .setView(view)
        .create()

    share.setOnClickListener {
        saveAndShareImage(context,bitmap)
        dialog.dismiss()
    }

    cancel.setOnClickListener {
        dialog.dismiss()
    }

    download.setOnClickListener {
        Log.d("TAG","download")
        saveImgInGallery(context,bitmap)
//        dialog.dismiss()
    }

    custom.setOnClickListener {
        Log.d("TAG","custom")
//        dialog.dismiss()
    }

    dialog.show()

}

fun saveImgInGallery(context: Context, bitmap: Bitmap) {

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








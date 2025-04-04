package com.example.quotesapp.presentation.home_screen

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import com.example.quotesapp.R
import com.example.quotesapp.domain.model.Quote
import java.io.File

fun createImageFromXml(context: Context, quote: Quote, callback: (Bitmap) -> Unit) {
    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.share_image, null, false)

    // Set the quote text dynamically
    val quoteTextView = view.findViewById<TextView>(R.id.quote_text)
    val authorTextView = view.findViewById<TextView>(R.id.quote_author)

    quoteTextView.text = quote.quote
    authorTextView.text = "- ${quote.author}"

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

        val dialog = AlertDialog.Builder(context)

        val imageView = ImageView(context).apply {
            setImageBitmap(bitmap)
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        dialog.setView(imageView)
            .setPositiveButton("Share") { _, _ ->
                saveAndShareImage(context, bitmap)
            }
            .setNegativeButton("Cancel", null)
            .show()

}








package com.subscaleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.InputStream

class SkiaImageDecoder : ImageDecoder {
    override fun decode(context: Context, uri: Uri): Bitmap {
        val uriString = uri.toString()
        val options = BitmapFactory.Options()
        val bitmap: Bitmap?
        options.inPreferredConfig = Bitmap.Config.RGB_565
        when {
            uriString.startsWith(SubsamplingScaleImageView.Companion.ASSET_PREFIX) -> {
                val assetName = uriString.substring(SubsamplingScaleImageView.Companion.ASSET_PREFIX.length)
                bitmap = BitmapFactory.decodeStream(context.assets.open(assetName), null, options)
            }
            else -> {
                var inputStream: InputStream? = null
                try {
                    val contentResolver = context.contentResolver
                    inputStream = contentResolver.openInputStream(uri)
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                } finally {
                    try {
                        inputStream?.close()
                    } catch (e: Exception) {
                    }
                }
            }
        }

        if (bitmap == null) {
            throw RuntimeException("Skia image region decoder returned null bitmap - image format may not be supported")
        }
        return bitmap
    }
}

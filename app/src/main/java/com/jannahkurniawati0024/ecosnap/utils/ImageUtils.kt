package com.jannahkurniawati0024.ecosnap.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageUtils {

    fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("ecosnap_img_", ".jpg", context.cacheDir)
            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            compressImage(context, tempFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun compressImage(context: Context, file: File, maxSizeKb: Int = 1024): File {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return file
        val outputFile = File(context.cacheDir, "compressed_${file.name}")
        var quality = 90
        FileOutputStream(outputFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        while (outputFile.length() / 1024 > maxSizeKb && quality > 20) {
            quality -= 10
            FileOutputStream(outputFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            }
        }
        bitmap.recycle()
        return outputFile
    }
}
package com.vickikbt.fixitapp.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ImageHelpers(private val context: Context) {

    private val applicationContext: Context = context.applicationContext

    fun getImageBody(selectedImage: Uri): MultipartBody.Part {
        val parcelFileDescriptor =
            applicationContext.contentResolver.openFileDescriptor(selectedImage, "r", null)

        val file = File(
            applicationContext.cacheDir,
            applicationContext.contentResolver.getFileName(selectedImage)
        )

        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("upload", file.name, requestBody)
    }

}
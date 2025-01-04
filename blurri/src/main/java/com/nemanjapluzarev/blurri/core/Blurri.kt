package com.nemanjapluzarev.blurri.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * The `Blurri` object provides utility functions to load images asynchronously
 * from various sources such as URLs, file paths, resource IDs, and input streams.
 *
 * It uses Kotlin coroutines to perform image loading on background threads
 * and delivers the results on the main thread.
 */

internal object Blurri {

    /**
     * A helper function to handle image loading logic.
     *
     * This function performs the image loading asynchronously using the provided
     * `load` lambda and manages success and error callbacks on the main thread.
     *
     * @param load A suspending lambda that returns a [Bitmap] or null if loading fails.
     * @param onSuccess Callback invoked when the image is successfully loaded, providing the loaded [Bitmap].
     * @param onError Callback invoked when an error occurs, providing an error message.
     */
    private suspend fun loadImage(
        load: suspend () -> Bitmap?,
        onSuccess: (Bitmap) -> Unit,
        onError: (String) -> Unit
    ) {
        runCatching {
            val bitmap = load()
            bitmap?.let { loadedBitmap ->
                withContext(Dispatchers.Main) {
                    onSuccess(loadedBitmap)
                }
            } ?: run {
                withContext(Dispatchers.Main) {
                    onError("Error in connection!")
                }
            }
        }.getOrElse { exception ->
            exception.printStackTrace()
            withContext(Dispatchers.Main) {
                onError("Error: ${exception.localizedMessage}")
            }
        }
    }

    /**
     * Loads an image from a string URL.
     *
     * @param url The URL of the image as a string.
     * @param onSuccess Callback invoked when the image is successfully loaded, providing the loaded [Bitmap].
     * @param onError Callback invoked when an error occurs, providing an error message.
     */
    suspend fun loadImageFromStringUrl(
        url: String,
        onSuccess: (Bitmap) -> Unit,
        onError: (String) -> Unit
    ) {
        loadImage(
            load = {
                withContext(Dispatchers.IO) {
                    val connection = (URL(url).openConnection() as HttpsURLConnection).apply {
                        doInput = true
                        connect()
                    }
                    val inputStream: InputStream = connection.inputStream
                    BitmapFactory.decodeStream(inputStream)
                }
            },
            onSuccess = onSuccess,
            onError = onError
        )
    }

    /**
     * Loads an image from a [URL] object.
     *
     * @param url The URL of the image.
     * @param onSuccess Callback invoked when the image is successfully loaded, providing the loaded [Bitmap].
     * @param onError Callback invoked when an error occurs, providing an error message.
     */
    suspend fun loadImageFromUrl(
        url: URL,
        onSuccess: (Bitmap) -> Unit,
        onError: (String) -> Unit
    ) {
        loadImage(
            load = {
                withContext(Dispatchers.IO) {
                    val connection = (url.openConnection() as HttpsURLConnection).apply {
                        doInput = true
                        connect()
                    }
                    val inputStream: InputStream = connection.inputStream
                    BitmapFactory.decodeStream(inputStream)
                }
            },
            onSuccess = onSuccess,
            onError = onError
        )
    }

    /**
     * Loads an image from a local file.
     *
     * @param filePath The [File] object representing the image file.
     * @param onSuccess Callback invoked when the image is successfully loaded, providing the loaded [Bitmap].
     * @param onError Callback invoked when an error occurs, providing an error message.
     */
    suspend fun loadImageFromFile(
        filePath: File,
        onSuccess: (Bitmap) -> Unit,
        onError: (String) -> Unit
    ) {
        loadImage(
            load = {
                withContext(Dispatchers.IO) {
                    BitmapFactory.decodeFile(filePath.absolutePath)
                }
            },
            onSuccess = onSuccess,
            onError = onError
        )
    }

    /**
     * Loads an image from a resource ID.
     *
     * @param resId The resource ID of the image.
     * @param context The [Context] used to access resources.
     * @param onSuccess Callback invoked when the image is successfully loaded, providing the loaded [Bitmap].
     * @param onError Callback invoked when an error occurs, providing an error message.
     */
    suspend fun loadImageFromResource(
        resId: Int,
        context: Context,
        onSuccess: (Bitmap) -> Unit,
        onError: (String) -> Unit
    ) {
        loadImage(
            load = {
                withContext(Dispatchers.IO) {
                    val resources = context.resources
                    BitmapFactory.decodeResource(resources, resId)
                }
            },
            onSuccess = onSuccess,
            onError = onError
        )
    }

    /**
     * Loads an image from an [InputStream].
     *
     * @param inputStream The [InputStream] containing the image data.
     * @param onSuccess Callback invoked when the image is successfully loaded, providing the loaded [Bitmap].
     * @param onError Callback invoked when an error occurs, providing an error message.
     */
    suspend fun loadImageFromStream(
        inputStream: InputStream,
        onSuccess: (Bitmap) -> Unit,
        onError: (String) -> Unit
    ) {
        loadImage(
            load = {
                withContext(Dispatchers.IO) {
                    BitmapFactory.decodeStream(inputStream)
                }
            },
            onSuccess = onSuccess,
            onError = onError
        )
    }
}
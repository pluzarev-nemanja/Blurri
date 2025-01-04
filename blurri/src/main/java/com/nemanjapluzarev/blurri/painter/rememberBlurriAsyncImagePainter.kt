package com.nemanjapluzarev.blurri.painter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.kumcompany.blurri.R
import com.nemanjapluzarev.blurri.core.Blurri
import java.io.File
import java.io.InputStream
import java.net.URL


/**
 * A composable function that provides a [State] containing a [Bitmap] loaded asynchronously from a given model.
 *
 * This function supports loading images from the following sources:
 * - String URL
 * - File
 * - Resource ID
 * - InputStream
 *
 * It uses the [Blurri] object to load the image in the background and updates the [State] when the image is loaded.
 * The resulting [Bitmap] can be used to display the image in the UI.
 *
 * @param model The source of the image. Supported types:
 * - [String]: A URL string representing the image source.
 * - [File]: A local file containing the image.
 * - [Int]: A resource ID of the image in the app's resources.
 * - [InputStream]: An input stream containing the image data.
 * @param context The [Context] used for resource loading. Defaults to [LocalContext.current].
 * @return A [State] object containing the loaded [Bitmap] or null if the image is not yet loaded or fails to load.
 *
 * @sample
 * ```kotlin
 * val imageBitmap by rememberBlurriAsyncImagePainter(model = "https://example.com/image.png").value
 * imageBitmap?.let {
 *     Image(bitmap = it.asImageBitmap(), contentDescription = "Loaded Image")
 * }
 * ```
 */
@Composable
internal fun rememberBlurriAsyncImagePainter(
    model: Any,
    context: Context = LocalContext.current,
): State<Bitmap?> {
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(model) {
        when (model) {
            is String -> Blurri.loadImageFromStringUrl(
                url = model,
                onSuccess = { bitmapState.value = it },
                onError = {
                    Log.e(
                        context.getString(R.string.blurri),
                        context.getString(R.string.error_loading_image, it)
                    )
                }
            )

            is URL -> Blurri.loadImageFromUrl(
                url = model,
                onSuccess = { bitmapState.value = it },
                onError = {
                    Log.e(
                        context.getString(R.string.blurri),
                        context.getString(R.string.error_loading_image, it)
                    )
                }
            )

            is File -> Blurri.loadImageFromFile(
                filePath = model,
                onSuccess = { bitmapState.value = it },
                onError = {
                    Log.e(
                        context.getString(R.string.blurri),
                        context.getString(R.string.error_loading_image, it)
                    )
                }
            )

            is Int -> Blurri.loadImageFromResource(
                resId = model,
                context = context,
                onSuccess = { bitmapState.value = it },
                onError = {
                    Log.e(
                        context.getString(R.string.blurri),
                        context.getString(R.string.error_loading_image, it)
                    )
                }
            )

            is InputStream -> Blurri.loadImageFromStream(
                inputStream = model,
                onSuccess = { bitmapState.value = it },
                onError = {
                    Log.e(
                        context.getString(R.string.blurri),
                        context.getString(R.string.error_loading_image, it)
                    )
                }
            )

            else -> Log.e(
                context.getString(R.string.blurri),
                context.getString(R.string.unsupported_source_type)
            )
        }
    }

    return bitmapState
}
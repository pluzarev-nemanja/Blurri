package com.nemanjapluzarev.blurri.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.nemanjapluzarev.blurri.extensions.applyBlur
import com.nemanjapluzarev.blurri.painter.rememberBlurriAsyncImagePainter

/**
 * A composable function that displays an image with support for asynchronous loading, placeholders,
 * error handling, and fallback images. It uses `rememberBlurriAsyncImagePainter` to load images
 * from various sources such as URLs, files, resources, or input streams.
 *
 * @param model The source of the image. Supported types include:
 * - [String]: A URL string representing the image source.
 * - [File]: A local file containing the image.
 * - [Int]: A resource ID of the image in the app's resources.
 * - [InputStream]: An input stream containing the image data.
 * @param modifier Modifier to apply to the image.
 * @param contentDescription A description of the image for accessibility purposes.
 * @param placeholder A [Painter] to display while the image is loading.
 * @param error A [Painter] to display if the image loading fails.
 * @param fallback A [Painter] to display if the image source is null or unsupported. Defaults to [error].
 * @param alignment The alignment of the content inside the container. Defaults to [Alignment.Center].
 * @param contentScale The scaling behavior for the image. Defaults to [ContentScale.Crop].
 * @param alpha The alpha value to apply to the image. Defaults to [DefaultAlpha].
 * @param colorFilter An optional [ColorFilter] to apply to the image.
 */
@Composable
@NonRestartableComposable
fun BlurriImage(
    model: Any,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    placeholder: Any? = null,
    error: Any? = null,
    fallback: Any? = error,
    blurRadius: Float = 0f,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) = BlurriImageInternal(
    model = model,
    modifier = modifier,
    blurRadius = blurRadius,
    contentDescription = contentDescription,
    placeholder = placeholder,
    error = error,
    fallback = fallback,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter
)

/**
 * An internal composable function that handles the logic for rendering the image.
 * It manages the following states:
 * - Loaded image: Displays the image when it is successfully loaded.
 * - Placeholder: Displays a placeholder while the image is being loaded.
 * - Error or Fallback: Displays an error or fallback image if loading fails or the source is null.
 * - Loading Indicator: Displays a circular progress indicator if no placeholder or fallback is provided.
 *
 * @param model The source of the image.
 * @param modifier Modifier to apply to the image.
 * @param contentDescription A description of the image for accessibility purposes.
 * @param placeholder A [Painter] to display while the image is loading.
 * @param error A [Painter] to display if the image loading fails.
 * @param fallback A [Painter] to display if the image source is null or unsupported. Defaults to [error].
 * @param alignment The alignment of the content inside the container.
 * @param contentScale The scaling behavior for the image.
 * @param alpha The alpha value to apply to the image.
 * @param colorFilter An optional [ColorFilter] to apply to the image.
 */
@Composable
private fun BlurriImageInternal(
    model: Any,
    modifier: Modifier,
    contentDescription: String?,
    placeholder: Any?,
    error: Any?,
    fallback: Any?,
    blurRadius: Float = 0f,
    alignment: Alignment,
    contentScale: ContentScale,
    alpha: Float,
    colorFilter: ColorFilter?,
) {
    val bitmap by rememberBlurriAsyncImagePainter(model)
    val context = LocalContext.current

    when {
        bitmap != null -> {

            val blurredBitmap =
                if (blurRadius > 0) bitmap!!.applyBlur(context, blurRadius) else bitmap!!

            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Image(
                    bitmap = blurredBitmap.asImageBitmap(),
                    contentDescription = contentDescription,
                    modifier = modifier,
                    alignment = alignment,
                    contentScale = contentScale,
                    alpha = alpha,
                    colorFilter = colorFilter
                )
            }
        }

        placeholder != null -> {
            val placeholderImage by rememberBlurriAsyncImagePainter(model = placeholder)
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                placeholderImage?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = contentDescription,
                        modifier = modifier,
                        alignment = alignment,
                        contentScale = contentScale,
                        alpha = alpha,
                        colorFilter = colorFilter
                    )
                }
            }
        }

        else -> {
            fallback?.let {

                val fallbackImage by rememberBlurriAsyncImagePainter(model = fallback)
                fallbackImage?.let {
                    Box(modifier = modifier, contentAlignment = Alignment.Center) {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = contentDescription,
                            modifier = modifier,
                            alignment = alignment,
                            contentScale = contentScale,
                            alpha = alpha,
                            colorFilter = colorFilter
                        )
                    }
                }
            } ?: Box(modifier = modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

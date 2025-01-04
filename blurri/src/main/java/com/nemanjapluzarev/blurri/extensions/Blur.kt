package com.nemanjapluzarev.blurri.extensions

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.FloatRange

/**
 * Applies a blur effect to the given [Bitmap].
 *
 * @param context The context for accessing RenderScript.
 * @param radius The blur radius (0–25).
 * @return A new [Bitmap] with the blur effect applied.
 */
internal fun Bitmap.applyBlur(context: Context, @FloatRange(from = 0.0, to = 25.0) radius: Float): Bitmap {
    require(radius in 0f..25f) { "Blur radius must be between 0 and 25." }

    val renderScript = RenderScript.create(context)

    val input = Allocation.createFromBitmap(renderScript, this)
    val output = Allocation.createTyped(renderScript, input.type)

    val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, input.element)
    scriptIntrinsicBlur.setInput(input)
    scriptIntrinsicBlur.setRadius(radius)
    scriptIntrinsicBlur.forEach(output)

    val blurredBitmap = Bitmap.createBitmap(this.width, this.height, this.config)
    output.copyTo(blurredBitmap)

    input.destroy()
    output.destroy()
    scriptIntrinsicBlur.destroy()
    renderScript.destroy()

    return blurredBitmap
}
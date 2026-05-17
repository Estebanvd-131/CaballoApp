package com.villalobos.caballoapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

object RegionImageComposer {

    fun createVerticalComposite(
        context: Context,
        @DrawableRes drawableResIds: List<Int>,
        targetWidth: Int,
        targetHeight: Int
    ): Drawable? {
        if (drawableResIds.isEmpty()) return null

        val safeTargetWidth = targetWidth.coerceAtLeast(1)
        val safeTargetHeight = targetHeight.coerceAtLeast(drawableResIds.size)
        val segmentHeight = (safeTargetHeight / drawableResIds.size).coerceAtLeast(1)

        val segmentBitmaps = drawableResIds.mapNotNull { resId ->
            decodeSampledBitmap(context, resId, safeTargetWidth, segmentHeight)
        }

        if (segmentBitmaps.isEmpty()) return null

        val compositeBitmap = Bitmap.createBitmap(
            safeTargetWidth,
            segmentHeight * segmentBitmaps.size,
            Bitmap.Config.RGB_565
        )

        val canvas = Canvas(compositeBitmap)
        segmentBitmaps.forEachIndexed { index, bitmap ->
            val top = index * segmentHeight
            val destination = android.graphics.Rect(0, top, safeTargetWidth, top + segmentHeight)
            canvas.drawBitmap(bitmap, null, destination, null)
        }

        return BitmapDrawable(context.resources, compositeBitmap)
    }

    private fun decodeSampledBitmap(
        context: Context,
        @DrawableRes resId: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap? {
        val boundsOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            inScaled = false
        }
        BitmapFactory.decodeResource(context.resources, resId, boundsOptions)

        if (boundsOptions.outWidth <= 0 || boundsOptions.outHeight <= 0) {
            return null
        }

        val decodeOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = false
            inPreferredConfig = Bitmap.Config.RGB_565
            inScaled = false
            inSampleSize = calculateInSampleSize(boundsOptions, targetWidth, targetHeight)
        }

        return BitmapFactory.decodeResource(context.resources, resId, decodeOptions)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val heightRatio = kotlin.math.ceil(options.outHeight.toDouble() / reqHeight.coerceAtLeast(1).toDouble()).toInt()
        val widthRatio = kotlin.math.ceil(options.outWidth.toDouble() / reqWidth.coerceAtLeast(1).toDouble()).toInt()
        val minRatio = maxOf(heightRatio, widthRatio).coerceAtLeast(1)

        var inSampleSize = 1
        while (inSampleSize < minRatio) {
            inSampleSize *= 2
        }

        return inSampleSize
    }
}
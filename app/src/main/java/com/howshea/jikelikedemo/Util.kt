@file:Suppress("NOTHING_TO_INLINE")

package com.howshea.jikelikedemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View

/**
 * Created by howshea
 * on 2017/11/3.
 */
inline fun View.dpToPx(dp: Float) = dp * resources.displayMetrics.density + 0.5f

inline fun View.createBitmap(res: Int):Bitmap = BitmapFactory.decodeResource(resources, res)
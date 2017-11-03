package com.howshea.jikelikedemo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.util.AttributeSet
import android.view.View

/**
 * PackageName: com.howshea.jikelikedemo
 * FileName：   LikeView
 * Created by haipo on 2017/11/2.
 */
class LikeView : View {
    private var isLike = false
    private var bitmapLike: Bitmap = createBitmap(R.drawable.ic_messages_like_unselected)
    private val bitmapShining = createBitmap(R.drawable.ic_messages_like_selected_shining)
    private var likeCount = 8
    private var textAlpha1 = 255
        set(value) {
            field = value
            invalidate()
        }

    private var textAlpha2 = 0
        set(value) {
            field = value
            invalidate()
        }
    private val paintText1 = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
        color = Color.parseColor("#BDBDBD")
        textSize = bitmapLike.height * 2 / 3f
    }

    private val paintText2 = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
        color = Color.parseColor("#BDBDBD")
        textSize = bitmapLike.height * 2 / 3f
    }

    private val paintBitmap = Paint().apply {

    }


    private var offsetY1: Float
        set(value) {
            field = value
            invalidate()
        }
    private var offsetY2: Float
        set(value) {
            field = value
            invalidate()
        }
    private var textBottom1: Float
    private var textBottom2: Float

    private val text1 = likeCount.toString(10)
    private val text2 = (likeCount + 1).toString(10)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        offsetY1 = (dpToPx(8f) + dpToPx(8f) + bitmapLike.height) / 2 + paintText1.run {
            -(fontMetrics.descent + fontMetrics.ascent) / 2f
        }
        offsetY2 = offsetY1 * 2
        textBottom1 = offsetY1
        textBottom2 = offsetY2
    }


    override fun onDraw(canvas: Canvas) {
        canvas.run {
            bitmapLike = if (!isLike) createBitmap(R.drawable.ic_messages_like_unselected) else createBitmap(R.drawable.ic_messages_like_selected)
            clipRect(0f, 0f, bitmapLike.width + dpToPx(20f), dpToPx(16f) + bitmapLike.height)
            drawBitmap(bitmapLike, dpToPx(8f), dpToPx(8f), paintBitmap)
            paintText1.alpha = textAlpha1
            paintText2.alpha = textAlpha2
            drawText(text1, bitmapLike.width + dpToPx(12f), offsetY1, paintText1)
            drawText(text2, bitmapLike.width + dpToPx(12f), offsetY2, paintText2)
        }
    }

    fun like() {
        if (!isLike) {
            animatorLike()
        } else {
            animatorUnlike()
        }
        isLike = !isLike

    }

    private fun animatorLike() {
        val animator1 = ObjectAnimator.ofFloat(this, "offsetY1", textBottom1, 0f)
        animator1.interpolator = FastOutLinearInInterpolator()
        val animator2 = ObjectAnimator.ofInt(this, "textAlpha1", 255, 0)
        val animator3 = ObjectAnimator.ofFloat(this, "offsetY2", textBottom2, textBottom1)
        val animator4 = ObjectAnimator.ofInt(this, "textAlpha2", 0, 255)

        AnimatorSet().run {
            duration = 500
            playTogether(animator1, animator2, animator3, animator4)
            start()
        }

    }

    private fun animatorUnlike() {
        val animator1 = ObjectAnimator.ofFloat(this, "offsetY1", 0f, textBottom1)
        val animator2 = ObjectAnimator.ofInt(this, "textAlpha1", 0, 255)
        val animator3 = ObjectAnimator.ofFloat(this, "offsetY2", textBottom1, textBottom2)
        animator3.interpolator = FastOutLinearInInterpolator()
        val animator4 = ObjectAnimator.ofInt(this, "textAlpha2", 255, 0)
        AnimatorSet().run {
            duration = 400
            playTogether(animator1, animator2, animator3, animator4)
            start()
        }
    }
}
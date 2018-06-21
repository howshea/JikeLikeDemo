package com.howshea.jikelikedemo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.*

/**
 * PackageName: com.howshea.jikelikedemo
 * FileName：   LikeView
 * Created by Howshea on 2017/11/2.
 */
class LikeView : View {
    private var isLike = false
    private var bitmapLike: Bitmap = createBitmap(R.drawable.ic_messages_like_unselected)
    private val bitmapShining = createBitmap(R.drawable.ic_messages_like_selected_shining)
    var likeCount = 0
        set(value) {
            field = value
            reInit()
        }
    private var textFrontWidth = 0f
    private var textFront: String = ""
    private var textUp: String = ""
    private var textBottom: String = ""
    private var radius = 1f
        set(value) {
            field = value
            invalidate()
        }
    private var circleAlpha = 0
        set(value) {
            field = value
            invalidate()
        }

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

    private val paintText0 = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
        color = Color.parseColor("#BDBDBD")
        textSize = bitmapLike.height * 2 / 3f
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
        isAntiAlias = true
    }

    private val paintBitmapShine = Paint().apply {
        isAntiAlias = true
    }

    private val paintCircle = Paint().apply {
        isAntiAlias = true
        @Suppress("DEPRECATION")
        color = resources.getColor(R.color.red)
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(2f)
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

    private var scaleXY = 1f
        set(value) {
            field = value
            invalidate()
        }

    private var shineAlpha = 0
        set(value) {
            field = value
            invalidate()
        }

    //temp
    private var textBottomY1: Float
    private var textBottomY2: Float
    private var textFrontX = bitmapLike.width + dpToPx(12f)
    private var textEndX: Float

    private var text1 = likeCount.toString(10)
    private var text2 = (likeCount + 1).toString(10)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        offsetY1 = (dpToPx(8f) + dpToPx(8f) + bitmapLike.height) / 2 + paintText1.run {
            -(fontMetrics.descent + fontMetrics.ascent) / 2f
        }
        offsetY2 = offsetY1 * 2
        textBottomY1 = offsetY1
        textBottomY2 = offsetY2
        sliceText(text1, text2)
        textFrontWidth = paintText0.measureText(textFront)
        textEndX = textFrontX + textFrontWidth
    }

    private fun reInit() {
        val count = if (isLike) likeCount - 1 else likeCount
        text1 = count.toString(10)
        text2 = (count + 1).toString(10)
        sliceText(text1, text2)
        textFrontWidth = paintText0.measureText(textFront)
        textEndX = textFrontX + textFrontWidth
        println("reInit, text1: $text1")
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val textWidth = paintText1.measureText(text2)
        val specModeX = getMode(widthMeasureSpec)
        val specModeY = getMode(heightMeasureSpec)
        val width = when (specModeX) {
            EXACTLY -> measuredWidth
            UNSPECIFIED, AT_MOST -> (dpToPx(20f) + bitmapLike.width + textWidth).toInt()
            else -> measuredWidth
        }
        val height = when (specModeY) {
            EXACTLY -> measuredHeight
            UNSPECIFIED, AT_MOST -> (dpToPx(8f) + bitmapLike.height * 0.8 * 2).toInt()
            else -> measuredHeight
        }
        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.run {
            bitmapLike = if (!isLike) createBitmap(R.drawable.ic_messages_like_unselected) else createBitmap(R.drawable.ic_messages_like_selected)
//            clipRect(0f, 0f, bitmapLike.width + dpToPx(20f), dpToPx(16f) + bitmapLike.height)
            save()
            scale(scaleXY, scaleXY, dpToPx(8f) + bitmapLike.width / 2, dpToPx(8f) + bitmapLike.height / 2)
            drawBitmap(bitmapLike, dpToPx(8f), dpToPx(8f), paintBitmap)
            restore()

            if (isLike) {
                save()
                paintBitmapShine.alpha = shineAlpha
                drawBitmap(bitmapShining, dpToPx(10f), dpToPx(0.5f), paintBitmapShine)
                restore()

                save()
                paintCircle.alpha = circleAlpha
                drawCircle(dpToPx(8f) + bitmapLike.width / 2f, dpToPx(6f) + bitmapLike.height / 2f, radius, paintCircle)
            }

            paintText1.alpha = textAlpha1
            paintText2.alpha = textAlpha2
            drawText(textFront, textFrontX, textBottomY1, paintText0)
            drawText(textUp, textEndX, offsetY1, paintText1)
            drawText(textBottom, textEndX, offsetY2, paintText2)
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
        val animator1 = ObjectAnimator.ofFloat(this, "offsetY1", textBottomY1, 0f)
        animator1.interpolator = FastOutLinearInInterpolator()
        val animator2 = ObjectAnimator.ofInt(this, "textAlpha1", 255, 0)
        val animator3 = ObjectAnimator.ofFloat(this, "offsetY2", textBottomY2, textBottomY1)
        val animator4 = ObjectAnimator.ofInt(this, "textAlpha2", 0, 255)
        val animator5 = ObjectAnimator.ofFloat(this, "scaleXY", 0.7f, 1f, 1.2f, 0.9f, 1f)
        val animator6 = ObjectAnimator.ofInt(this, "shineAlpha", 0, 255)
        val animator7 = ObjectAnimator.ofFloat(this, "radius", 0f, bitmapLike.width * 0.8f)
        val animator8 = ObjectAnimator.ofInt(this, "circleAlpha", 0, 50, 100, 0)
        AnimatorSet().run {
            duration = 350
            playTogether(animator1, animator2, animator3, animator4, animator5, animator6, animator7, animator8)
            start()
        }

    }

    private fun animatorUnlike() {
        val animator1 = ObjectAnimator.ofFloat(this, "offsetY1", 0f, textBottomY1)
        val animator2 = ObjectAnimator.ofInt(this, "textAlpha1", 0, 255)
        val animator3 = ObjectAnimator.ofFloat(this, "offsetY2", textBottomY1, textBottomY2)
        animator3.interpolator = FastOutLinearInInterpolator()
        val animator4 = ObjectAnimator.ofInt(this, "textAlpha2", 255, 0)
        val animator5 = ObjectAnimator.ofFloat(this, "scaleXY", 1f, 0.7f, 1f)
        AnimatorSet().run {
            duration = 350
            playTogether(animator1, animator2, animator3, animator4, animator5)
            start()
        }
    }


    private fun sliceText(text1: String, text2: String) {
        textFront = text1.takeWhileIndexed { i, c ->
            c == text2[i]
        }
        textUp = text1.substringAfter(textFront)
        textBottom = text2.substringAfter(textFront)
    }

}

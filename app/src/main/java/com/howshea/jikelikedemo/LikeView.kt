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

/**
 * PackageName: com.howshea.jikelikedemo
 * FileName：   LikeView
 * Created by Howshea on 2017/11/2.
 */
class LikeView : View {
    private var isLike = false
    private var bitmapLike: Bitmap = createBitmap(R.drawable.ic_messages_like_unselected)
    private val bitmapShining = createBitmap(R.drawable.ic_messages_like_selected_shining)
    var likeCount = 10
        set(value) {
            field = value
            reInit()
        }
    private var carryOver = false
    private var textFrontWidth = 0f
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
    private var textBottom1: Float
    private var textBottom2: Float

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
        textBottom1 = offsetY1
        textBottom2 = offsetY2

        if (text1[0] == text2[0]) {
            carryOver = false
            textFrontWidth = paintText0.measureText(text1[0].toString())
        } else {
            carryOver = true
        }


    }

    private fun reInit() {
        val count = if (isLike) likeCount - 1 else likeCount
        text1 = count.toString(10)
        text2 = (count + 1).toString(10)
        if (text1[0] == text2[0]) {
            carryOver = false
            textFrontWidth = paintText0.measureText(text1[0].toString())
        } else {
            carryOver = true
        }
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val textWidth = paintText1.measureText(text2)
        val width = dpToPx(20f) + bitmapLike.width + textWidth
        val height = dpToPx(8f) + bitmapLike.height * 0.8 * 2
        setMeasuredDimension(width.toInt(), height.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
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
            if (carryOver) {
                drawText(text1, bitmapLike.width + dpToPx(12f), offsetY1, paintText1)
                drawText(text2, bitmapLike.width + dpToPx(12f), offsetY2, paintText2)
            } else {
                drawText(text1[0].toString(), bitmapLike.width + dpToPx(12f), textBottom1, paintText0)
                drawText(text1[1].toString(), bitmapLike.width + dpToPx(12f) + textFrontWidth, offsetY1, paintText1)
                drawText(text2[1].toString(), bitmapLike.width + dpToPx(12f) + textFrontWidth, offsetY2, paintText2)
            }

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
        val animator1 = ObjectAnimator.ofFloat(this, "offsetY1", 0f, textBottom1)
        val animator2 = ObjectAnimator.ofInt(this, "textAlpha1", 0, 255)
        val animator3 = ObjectAnimator.ofFloat(this, "offsetY2", textBottom1, textBottom2)
        animator3.interpolator = FastOutLinearInInterpolator()
        val animator4 = ObjectAnimator.ofInt(this, "textAlpha2", 255, 0)
        val animator5 = ObjectAnimator.ofFloat(this, "scaleXY", 1f, 0.7f, 1f)
        AnimatorSet().run {
            duration = 350
            playTogether(animator1, animator2, animator3, animator4, animator5)
            start()
        }
    }
}
package com.howshea.jikelikedemo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by Howshea
 * on 2018/6/21.
 */
class JumpNumTextView : View {
    companion object {
        //默认字体颜色
        private const val DEFAULT_TEXT_COLOR = "#C3C3C3"
    }

    //是否处于点赞状态
    private var isLiked = false

    private var likeCount = 0

    private var textSize = 0f
    private var textColor = 0
    //文字拆成三个部分
    private var textFixed = ""
    private var textTop = ""
    private var textBottom = ""
    //进位
    private var carryFlag = false

    private val textPaintFixed = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
    }
    private val textTopPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
    }
    private val textBottomPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
    }

    //下面四个属性用于属性动画
    private var textTopY = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var textBottomY = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var textTopAlpha = 255
        set(value) {
            field = value
            invalidate()
        }
    private var textBottomAlpha = 0
        set(value) {
            field = value
            invalidate()
        }

    //固定文字部分末尾X轴坐标
    private val textFixedEndX
        get() = paddingStart + textPaintFixed.measureText(textFixed)

    //文字起始X轴坐标
    private val textStartX
        get() = paddingStart


    //文字的基准线
    private val baseLine
        get() = paddingTop - textPaintFixed.fontMetrics.top


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.JumpNumTextView)
        attributes?.apply {
            textSize = getDimension(R.styleable.JumpNumTextView_textSize, 0f)
            textColor = getColor(R.styleable.JumpNumTextView_textColor, Color.parseColor(DEFAULT_TEXT_COLOR))
        }
        attributes?.recycle()
        //设置paint的绘制参数
        setPaint()
        setText()
    }

    private fun setPaint() {
        textPaintFixed.run {
            color = textColor
            textSize = this@JumpNumTextView.textSize
        }
        textTopPaint.run {
            color = textColor
            textSize = this@JumpNumTextView.textSize
        }
        textBottomPaint.run {
            color = textColor
            textSize = this@JumpNumTextView.textSize
        }

    }

    //分割字符串
    private fun setText() {
        val count = if (isLiked) likeCount - 1 else likeCount
        if (count < 0) {
            Log.e("JumNumTextView", "最小数字必须大于等于0")
            return
        }
        sliceText(count.toString(10), (count + 1).toString(10))
        if (isLiked) {
            textTopY = 0f
            textBottomY = baseLine
            textTopAlpha = 0
            textBottomAlpha = 255
        } else {
            textTopY = baseLine
            textBottomY = baseLine * 2
            textTopAlpha = 255
            textBottomAlpha = 0
        }
        if (likeCount < 2) {
            if (textTop == "0") textTop = " "
            if (textBottom == "0") textBottom = " "
        }
        requestLayout()
    }

    fun initView(isLiked: Boolean, likeCount: Int) {
        this.isLiked = isLiked
        this.likeCount = likeCount
        setText()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = resolveSize(onMeasureWidth(), widthMeasureSpec)
        val measureHeight = resolveSize(onMeasureHeight(), heightMeasureSpec)
        setMeasuredDimension(measureWidth, measureHeight)
    }

    private fun onMeasureWidth() = paddingStart + paddingEnd + textPaintFixed.measureText(textFixed + textBottom).toInt()

    /**
     * 算出descent的Y轴坐标再加上paddingBottom
     */
    private fun onMeasureHeight() = baseLine.toInt() + textPaintFixed.fontMetricsInt.descent + paddingBottom

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            textTopPaint.alpha = textTopAlpha
            textBottomPaint.alpha = textBottomAlpha
            drawText(textFixed, textStartX.toFloat(), baseLine, textPaintFixed)
            //如果进位了textTop需要往后退一个字节的位置
            val textTopX = if (carryFlag)
                textFixedEndX + textPaintFixed.measureText(textBottom[0].toString())
            else
                textFixedEndX
            drawText(textTop, textTopX, textTopY, textTopPaint)
            drawText(textBottom, textFixedEndX, textBottomY, textBottomPaint)
        }
    }

    /**
     * 切割数字成三份，不变的部分固定
     */
    private fun sliceText(text1: String, text2: String) {
        carryFlag = text1.length != text2.length
        textFixed = text1.takeWhileIndexed { i, c ->
            c == text2[i]
        }
        textTop = text1.substringAfter(textFixed)
        textBottom = text2.substringAfter(textFixed)
    }

    private fun animatorLike() {
        val animatorGone = ObjectAnimator.ofFloat(this, "textTopY", baseLine, 0f)
        animatorGone.interpolator = FastOutLinearInInterpolator()
        val animatorHide = ObjectAnimator.ofInt(this, "textTopAlpha", 255, 0)
        val animatorVisible = ObjectAnimator.ofFloat(this, "textBottomY", baseLine * 2, baseLine)
        val animatorShow = ObjectAnimator.ofInt(this, "textBottomAlpha", 0, 255)
        AnimatorSet().run {
            duration = 350
            playTogether(animatorGone, animatorHide, animatorVisible, animatorShow)
            start()
        }
    }

    private fun animatorCancelLike() {
        val animatorVisible = ObjectAnimator.ofFloat(this, "textTopY", 0f, baseLine)
        animatorVisible.interpolator = FastOutLinearInInterpolator()
        val animatorShow = ObjectAnimator.ofInt(this, "textTopAlpha", 0, 255)
        val animatorGone = ObjectAnimator.ofFloat(this, "textBottomY", baseLine, baseLine * 2)
        val animatorHide = ObjectAnimator.ofInt(this, "textBottomAlpha", 255, 0)
        AnimatorSet().run {
            duration = 350
            playTogether(animatorVisible, animatorShow, animatorGone, animatorHide)
            start()
        }
    }

    fun like() {
        if (!isLiked) {
            animatorLike()
            likeCount++
        } else {
            animatorCancelLike()
            likeCount--
        }
        isLiked = !isLiked

    }

}


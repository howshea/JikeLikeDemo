package com.howshea.jikelikedemo

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout

/**
 * Created by 陶海啸
 * on 2018/6/22.
 */
class CommentLikeLayout : LinearLayout {
    //点赞状态
    var isLiked = false

    //挂起状态时不再响应事件
    private var isSuspended = false

    private var tvCount: JumpNumTextView? = null
    private var imgIcon: ImageView? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        orientation = HORIZONTAL
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 拦截事件向下分发
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?) = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 2) {
            throw IllegalArgumentException("不允许超过两个child")
        }
        when {
            getChildAt(0) is JumpNumTextView -> { //数字在左边
                tvCount = getChildAt(0) as JumpNumTextView
                imgIcon = getChildAt(1) as ImageView
            }
            getChildAt(0) is ImageView -> { //数字在右边
                tvCount = getChildAt(1) as JumpNumTextView
                imgIcon = getChildAt(0) as ImageView
            }
            else -> throw IllegalArgumentException("child必须是JumpNumTextView或者ImageView")
        }
    }

    fun handleFinished(isSucceed: Boolean) {
        isSuspended = false
        if (isSucceed) {
            tvCount?.like()
            like()
            isLiked = !isLiked
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        isSuspended = true
        super.setOnClickListener(l)
    }

    @JvmOverloads
    fun initView(likeCount: Int, isLiked: Boolean = this.isLiked) {
        tvCount?.initView(isLiked, likeCount)
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun animSelectIcon() {
        imgIcon?.isSelected = true
        val holderX = PropertyValuesHolder.ofFloat("scaleX", 0.7f, 1f, 1.2f, 0.9f, 1f)
        val holderY = PropertyValuesHolder.ofFloat("scaleY", 0.7f, 1f, 1.2f, 0.9f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(imgIcon, holderX, holderY).apply {
            duration = 350
            start()
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun animUnSelectIcon() {
        imgIcon?.isSelected = false
        val holderX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.7f, 1f)
        val holderY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.7f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(imgIcon, holderX, holderY).apply {
            duration = 350
            start()
        }
    }

    private fun like(){
        if (isLiked){
            animUnSelectIcon()
        } else {
            animSelectIcon()
        }
    }
}
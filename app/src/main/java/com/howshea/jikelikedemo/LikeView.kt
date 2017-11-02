package com.howshea.jikelikedemo

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * PackageName: com.howshea.jikelikedemo
 * FileName：   LikeView
 * Created by haipo on 2017/11/2.
 */
class LikeView : View {
    private var likeCount = 0
    private val paint = Paint().apply {
        isAntiAlias = true
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}
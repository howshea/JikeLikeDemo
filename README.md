# JikeLikeDemo
即刻app的点赞效果模仿

![](https://github.com/howshea/JikeLikeDemo/raw/master/gif/ezgif-1-966b8fd783.gif)

上面的那个是前期写的，写成一个view了，扩展性不好

下面那个是最近写的，分成了一个view和一个viewGroup，因为是viewGroup，所以可以调整图片和文字的左右位置以及图片的资源

## 用法

布局：

```xml
<com.howshea.jikelikedemo.CommentLikeLayout
     android:id="@+id/layout_like"
     android:layout_width="wrap_content"
     android:layout_height="50dp"
     android:layout_marginTop="20dp"
     android:gravity="center_vertical"
     android:padding="10dp">
     <com.howshea.jikelikedemo.JumpNumTextView
         android:layout_width="wrap_content"
         android:layout_height="wrap_content"
         android:paddingEnd="5dp"
         app:textSize="12sp" />
     <ImageView
         android:layout_width="15dp"
         android:layout_height="15dp"
         android:src="@drawable/ic_like" />
</com.howshea.jikelikedemo.CommentLikeLayout>
```

调用（kotlin）：

```kotlin
layout_like.initView(99, false)
layout_like.setOnClickListener {
    //网络请求写在这里
}

//假设这是网络请求成功的回调方法
fun thumbSucceed(){
    layout_like.handleFinished(true)
}
```

因为点赞这个动作一般是需要做网络请求，所有click事件执行的时候会进入挂起状态，不管成功或者失败，都要调用`handleFinished(isSucceed:Boolean)`去取消组件的挂起状态。

## 思路

难点主要是在于如何实现跳动的字符，要算出三个部分，一、不动的字符，例如从100 -> 101,不动的字符是10；另外两个部分是有动画的字符（如gif所示）。

主要算法如下：

```kotlin
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

/**
 * 修改takeWhile,加上index
 */
inline fun String.takeWhileIndexed(predicate: (index: Int, Char) -> Boolean): String {
    var index = 0
    return (0 until length)
        .firstOrNull { !predicate(index++, get(it)) }
        ?.let { substring(0, it) }
        ?: this
}
    
```

另外要考虑当数字位数发生变化、右边在图标左右的情况，文字的绘制位置要相应的作出调整，图标在数字右边时，数字的最后一位对齐，在左边时，数字的第一位对齐：

```kotlin
//如果进位了而且数字在图片左边，textTop需要往后退一个字节的位置
val textTopX = if (carryFlag && isOnLeft)
	textFixedEndX + textPaintFixed.measureText(textBottom[0].toString())
else
	textFixedEndX
```


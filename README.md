# JikeLikeDemo
即刻app的点赞效果模仿

![](https://github.com/howshea/JikeLikeDemo/raw/master/gif/ezgif-1-966b8fd783.gif)

上面的那个是前期写的，写成一个view了，扩展性不好

下面那个是最近写的，分成了一个view和一个viewGroup，因为是viewGroup，所以可以调整图片和文字的左右位置以及图片的资源

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

用法（kotlin）：

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

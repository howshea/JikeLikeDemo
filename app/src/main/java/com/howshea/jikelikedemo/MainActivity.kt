package com.howshea.jikelikedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                tv_like.initView(tv_like.isLiked, editText.text.toString().toLong().toInt())
            }

        }
        view.setOnClickListener {
            view.like()
        }
        tv_like.initView(true, 99)
        tv_like.setOnClickListener {
            tv_like.like()
        }
    }
}

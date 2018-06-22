package com.howshea.jikelikedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                layout_like.initView(editText.text.toString().toLong().toInt())
            }

        }
        view.setOnClickListener {
            view.like()
        }
        layout_like.initView(99, false)
        layout_like.setOnClickListener {
            layout_like.handleFinished(true)
        }
    }
}

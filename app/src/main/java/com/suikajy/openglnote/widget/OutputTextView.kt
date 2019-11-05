package com.suikajy.openglnote.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class OutputTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.BLACK)
        setPadding(dip(15), dip(15), dip(15), dip(15))
    }

    private fun View.dip(value: Int): Int = (value * context.resources.displayMetrics.density).toInt()
}
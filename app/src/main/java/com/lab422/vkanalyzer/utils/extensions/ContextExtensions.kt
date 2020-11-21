package com.lab422.vkanalyzer.utils.extensions

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

fun Context.getScreenWidth(): Int {
    val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}
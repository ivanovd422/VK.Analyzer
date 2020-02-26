package com.lab422.vkanalyzer.utils.extensions

import android.view.View

fun View.setVisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.show() {
    this.setVisible(true)
}

fun View.hide() {
    this.setVisible(false)
}
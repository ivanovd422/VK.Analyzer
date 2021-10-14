package com.lab422.vkanalyzer.utils.extensions

import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setActive(position: Int) {
    menu.getItem(position)?.isChecked = true
}

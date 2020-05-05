package com.lab422.vkanalyzer.utils.extensions

import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setActive(position: Int) {
    menu.getItem(position)?.isChecked = true
}

private fun BottomNavigationView.getItemView(position: Int): BottomNavigationItemView {
    val menuView = getChildAt(0) as BottomNavigationMenuView
    return menuView.getChildAt(position) as BottomNavigationItemView
}
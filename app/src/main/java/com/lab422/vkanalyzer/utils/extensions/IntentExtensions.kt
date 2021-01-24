package com.lab422.vkanalyzer.utils.extensions

import android.content.Intent

fun Intent.addFlagsNewTaskWithClear() {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    addFlagNewTask()
}

fun Intent.addFlagNewTask() {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

fun Intent.addFlagClearTop() {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
}

fun Intent.addFlagNoHistory() {
    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
}

fun Intent.addFlagSingleTop() {
    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
}

fun Intent.addFlagBroughtToFront() {
    addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
}

fun Intent.addFlagReorderToFront() {
    addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
}

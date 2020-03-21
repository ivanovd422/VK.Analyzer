package com.lab422.vkanalyzer.utils.extensions

import android.app.Activity
import com.lab422.vkanalyzer.R

fun Activity.overridePendingTransitionAsSlideOut(){
    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
}

fun Activity.overridePendingTransitionAsTopOut(){
    overridePendingTransition(R.anim.stay_anim, R.anim.trans_down_in)
}

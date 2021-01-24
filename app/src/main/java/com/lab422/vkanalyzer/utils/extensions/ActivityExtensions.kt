package com.lab422.vkanalyzer.utils.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.lab422.vkanalyzer.R

fun Activity.overridePendingTransitionAsSlideOut() {
    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
}

fun Activity.overridePendingTransitionAsTopOut() {
    overridePendingTransition(R.anim.stay_anim, R.anim.trans_down_in)
}

private const val VK_APP_PACKAGE_ID = "com.vkontakte.android"

fun Activity.openLink(url: String?) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val resInfo = packageManager.queryIntentActivities(intent, 0)
    if (resInfo.isEmpty()) return
    for (info in resInfo) {
        if (info.activityInfo == null) continue
        if (VK_APP_PACKAGE_ID == info.activityInfo.packageName) {
            intent.setPackage(info.activityInfo.packageName)
            break
        }
    }
    startActivity(intent)
}

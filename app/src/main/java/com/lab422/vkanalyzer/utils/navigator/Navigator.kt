package com.lab422.vkanalyzer.utils.navigator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.friendsList.FriendsListActivity
import com.lab422.vkanalyzer.ui.loading.LoadingActivity
import com.lab422.vkanalyzer.ui.login.LoginActivity
import com.lab422.vkanalyzer.ui.mainScreen.MainActivity
import com.lab422.vkanalyzer.ui.mutualFriends.MutualFriendsActivity
import com.lab422.vkanalyzer.utils.extensions.addFlagClearTop
import com.lab422.vkanalyzer.utils.extensions.addFlagNewTask
import com.lab422.vkanalyzer.utils.extensions.addFlagsNewTaskWithClear

class Navigator(val context: Context) {

    fun openMainActivity() {
        val intent = MainActivity.createIntent(context)
        intent.addFlagsNewTaskWithClear()
        intent.addFlagClearTop()
        context.startActivity(intent)
    }

    fun openLoginActivity() {
        val intent = LoginActivity.createIntent(context)
        intent.addFlagsNewTaskWithClear()
        openActivity(intent)
    }

    fun openLoadingActivity() {
        val intent = LoadingActivity.createIntent(context)
        intent.addFlagsNewTaskWithClear()
        intent.addFlagClearTop()
        context.startActivity(intent)
    }

    fun openMutualListActivity(firstId: String, secondId: String) {
        val intent = MutualFriendsActivity.createIntent(context, firstId, secondId)
        openActivity(intent)
    }

    fun openFriendsList(activity: FragmentActivity, requestCode: Int) {
        val intent = FriendsListActivity.createIntent(activity)
        openActivityForResult(activity, intent, requestCode)
    }
}

private fun Navigator.openActivity(intent: Intent, animation: Bundle? = makeSlideInAnimation()) {
    intent.addFlagNewTask()
    openActivityWithoutTask(intent, animation)
}

private fun Navigator.openActivityForResult(
    activity: FragmentActivity,
    intent: Intent,
    resultCode: Int,
    animation: Bundle? = makeSlideInAnimation()
) {
    openActivityForResultWithoutTask(activity, intent, resultCode, animation)
}

private fun Navigator.openActivityWithoutTask(intent: Intent, animation: Bundle? = makeSlideInAnimation()) {
    context.startActivity(intent, animation)
}

private fun Navigator.openActivityForResultWithoutTask(
    activity: FragmentActivity,
    intent: Intent,
    resultCode: Int,
    animation: Bundle? = makeSlideInAnimation()
) {
    activity.startActivityForResult(intent, resultCode, animation)
}

fun Navigator.makeNoneAnimation(): Bundle? =
    ActivityOptionsCompat.makeCustomAnimation(context, 0, 0).toBundle()

fun Navigator.makeSlideOutAnimation(): Bundle? =
    ActivityOptionsCompat.makeCustomAnimation(context, R.anim.stay_anim, R.anim.trans_down_in).toBundle()

fun Navigator.makeSlideInAnimation(): Bundle? =
    ActivityOptionsCompat.makeCustomAnimation(context, R.anim.trans_left_in, R.anim.trans_left_out).toBundle()

fun Navigator.makeTopInAnimation(): Bundle? =
    ActivityOptionsCompat.makeCustomAnimation(context, R.anim.trans_up_in, R.anim.stay_anim).toBundle()
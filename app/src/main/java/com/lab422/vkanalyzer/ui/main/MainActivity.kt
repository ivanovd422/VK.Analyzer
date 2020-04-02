package com.lab422.vkanalyzer.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.amplitude.api.Amplitude
import com.crashlytics.android.Crashlytics
import com.google.firebase.crashlytics.CrashlyticsRegistrar
import com.google.firebase.crashlytics.core.CrashlyticsCore
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.ui.friends.FriendModel
import com.lab422.vkanalyzer.ui.friends.FriendsActivity
import com.lab422.vkanalyzer.utils.extensions.afterTextChanged
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.viewState.isError
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.get

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by inject()
    private val navigator: Navigator = get()

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity()::class.java)
        }

        private const val REQUEST_CODE_GET_FIRST_FRIEND = 10001
        private const val REQUEST_CODE_GET_SECOND_FRIEND = 10002
    }

    override fun getToolBarViewId(): Int = R.id.toolbar_main

    override val toolbarName: Int = R.string.main_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar(false)

        initViews()
        initObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GET_FIRST_FRIEND || requestCode == REQUEST_CODE_GET_SECOND_FRIEND) {
            processData(requestCode, resultCode, data)
        }
    }

    private fun initViews() {
        btn_find_friends.setOnClickListener {
            viewModel.onSearchClicked(
                et_first_user.text.toString(),
                et_second_user.text.toString()
            )
        }

        iv_from_friends_list_first.setOnClickListener { openFriendsList(REQUEST_CODE_GET_FIRST_FRIEND) }
        iv_from_friends_list_second.setOnClickListener { openFriendsList(REQUEST_CODE_GET_SECOND_FRIEND) }

        et_first_user.afterTextChanged{ viewModel.onFirstIdEntered(it) }
        et_second_user.afterTextChanged{viewModel.onSecondIdEntered(it) }
    }

    private fun initObservers() {
        viewModel.getState().observe(this, Observer { viewState ->
            if (viewState.isError()) {
                Toast.makeText(this, viewState.error, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getFirstUserName().observe(this, Observer { name ->
            tv_first_user_name.setVisible(name.isNotEmpty())
            tv_first_user_name.text = name
        })

        viewModel.getSecondUserName().observe(this, Observer { name ->
            tv_second_user_name.setVisible(name.isNotEmpty())
            tv_second_user_name.text = name
        })
    }

    private fun openFriendsList(code: Int) {
        Amplitude.getInstance().logEvent("get user from friend list");
        navigator.openFriendsList(this, code)
    }

    private fun processData(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (data == null) {
                    return
                }
                data.extras?.getSerializable(FriendsActivity.FRIEND_ID_KEY)?.let {friend ->
                    friend as FriendModel
                    if (requestCode == REQUEST_CODE_GET_FIRST_FRIEND) {
                        et_first_user.setText(friend.id.toString())
                        viewModel.onFirstFriendFromList(friend)
                    } else {
                        et_second_user.setText(friend.id.toString())
                        viewModel.onSecondFriendFromList(friend)
                    }
                }
            }
            else -> {
            }
        }
    }
}

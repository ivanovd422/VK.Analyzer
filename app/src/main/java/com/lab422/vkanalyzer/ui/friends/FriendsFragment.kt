package com.lab422.vkanalyzer.ui.friends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lab422.common.viewState.isError
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.friendsList.FriendModel
import com.lab422.vkanalyzer.ui.friendsList.FriendsListActivity
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.extensions.afterTextChanged
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.navigator.Navigator
import kotlinx.android.synthetic.main.fragment_friends.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class FriendsFragment : Fragment(R.layout.fragment_friends) {

    private val viewModel: FriendsViewModel by inject()
    private val navigator: Navigator = get()
    private val tracker: TrackerService by inject()

    companion object {
        const val TAG = "FriendsFragment"
        fun newInstance() = FriendsFragment()

        private const val REQUEST_CODE_GET_FIRST_FRIEND = 10001
        private const val REQUEST_CODE_GET_SECOND_FRIEND = 10002
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

        til_first_user.setEndIconOnClickListener { openFriendsList(REQUEST_CODE_GET_FIRST_FRIEND) }
        til_second_user.setEndIconOnClickListener { openFriendsList(REQUEST_CODE_GET_SECOND_FRIEND) }

        et_first_user.afterTextChanged { viewModel.onFirstIdEntered(it) }
        et_second_user.afterTextChanged { viewModel.onSecondIdEntered(it) }
    }

    private fun initObservers() {
        viewModel.getState().observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState.isError()) {
                Toast.makeText(activity, viewState.error, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getFirstUserName().observe(viewLifecycleOwner, Observer { name ->
            tv_first_user_name.setVisible(name.isNotEmpty())
            tv_first_user_name.text = name
        })

        viewModel.getSecondUserName().observe(viewLifecycleOwner, Observer { name ->
            tv_second_user_name.setVisible(name.isNotEmpty())
            tv_second_user_name.text = name
        })
    }

    private fun openFriendsList(code: Int) {
        activity?.let {
            tracker.getUserFromFriendListClicked()
            val intent = FriendsListActivity.createIntent(it)
            startActivityForResult(intent, code)
        }
    }

    private fun processData(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (data == null) {
                    return
                }
                data.extras?.getSerializable(FriendsListActivity.FRIEND_ID_KEY)?.let { friend ->
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

package com.lab422.vkanalyzer.ui.friends

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lab422.common.viewState.isError
import com.lab422.vkanalyzer.databinding.FragmentFriendsBinding
import com.lab422.vkanalyzer.ui.friendsList.FriendModel
import com.lab422.vkanalyzer.ui.friendsList.FriendsListActivity
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.extensions.afterTextChanged
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.navigator.Navigator
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class FriendsFragment : Fragment() {

    private val viewModel: FriendsViewModel by inject()
    private val navigator: Navigator = get()
    private val tracker: TrackerService by inject()

    companion object {
        const val TAG = "FriendsFragment"
        fun newInstance() = FriendsFragment()

        private const val REQUEST_CODE_GET_FIRST_FRIEND = 10001
        private const val REQUEST_CODE_GET_SECOND_FRIEND = 10002
    }

    private lateinit var binding: FragmentFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GET_FIRST_FRIEND || requestCode == REQUEST_CODE_GET_SECOND_FRIEND) {
            processData(requestCode, resultCode, data)
        }
    }

    private fun initViews() {
        with(binding) {
            btnFindFriends.setOnClickListener {
                viewModel.onSearchClicked(
                    etFirstUser.text.toString(),
                    etSecondUser.text.toString()
                )
            }

            tilFirstUser.setEndIconOnClickListener { openFriendsList(REQUEST_CODE_GET_FIRST_FRIEND) }
            tilSecondUser.setEndIconOnClickListener { openFriendsList(REQUEST_CODE_GET_SECOND_FRIEND) }

            etFirstUser.afterTextChanged { viewModel.onFirstIdEntered(it) }
            etSecondUser.afterTextChanged { viewModel.onSecondIdEntered(it) }
        }
    }

    private fun initObservers() {
        viewModel.getState().observe(
            viewLifecycleOwner,
            { viewState ->
                if (viewState.isError()) {
                    Toast.makeText(activity, viewState.error, Toast.LENGTH_SHORT).show()
                }
            }
        )

        viewModel.getFirstUserName().observe(
            viewLifecycleOwner,
            { name ->
                binding.tvFirstUserName.apply {
                    setVisible(name.isNotEmpty())
                    text = name
                }
            }
        )

        viewModel.getSecondUserName().observe(
            viewLifecycleOwner,
            { name ->
                binding.tvSecondUserName.apply {
                    setVisible(name.isNotEmpty())
                    text = name
                }
            }
        )
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
                        binding.etFirstUser.setText(friend.id.toString())
                        viewModel.onFirstFriendFromList(friend)
                    } else {
                        binding.etSecondUser.setText(friend.id.toString())
                        viewModel.onSecondFriendFromList(friend)
                    }
                }
            }
            else -> {
            }
        }
    }
}

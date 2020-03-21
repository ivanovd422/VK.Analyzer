package com.lab422.vkanalyzer.ui.friends

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.ui.base.BaseItemDecoration
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.friends.adapter.FriendsListAdapter
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendViewHolder
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.utils.extensions.hide
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.stringProvider.StringProvider
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.lab422.vkanalyzer.utils.viewState.isError
import com.lab422.vkanalyzer.utils.viewState.isLoading
import com.lab422.vkanalyzer.utils.viewState.isSuccess
import kotlinx.android.synthetic.main.activity_friends_list.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel

class FriendsActivity : BaseActivity(R.layout.activity_friends_list), FriendViewHolder.Listener {

    private lateinit var viewModel: FriendsViewModel

    private val stringProvider: StringProvider = get()
    private var activityLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    private var friendsAdapter: FriendsListAdapter

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, FriendsActivity()::class.java)
        const val FRIEND_ID_KEY = "friend_id_key"
    }

    init {
        friendsAdapter = FriendsListAdapter(listOf(), stringProvider, this, this)
    }

    override fun getToolBarViewId(): Int = R.id.toolbar_friends_list

    override val toolbarName: Int = R.string.friends_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar()
        viewModel = getViewModel()
        initViews()
        initObservers()
    }

    override fun onFriendClicked(id: Long) {
        setResult(Activity.RESULT_OK, Intent().putExtra(FRIEND_ID_KEY, id))
        finish()
    }

    private fun initViews() {
        ll_friends_list_error_wrapper.hide()
        btn_friends_list_return.setOnClickListener { finish() }

        rv_friends_list.run {
            layoutManager = activityLayoutManager
            adapter = friendsAdapter
        }

        val divider = BaseItemDecoration(this)
        rv_friends_list.addItemDecoration(divider, 0)
    }

    private fun initObservers() {
        viewModel.getFriendsState().observe(this, Observer { viewState ->
            processState(viewState)
        })
    }

    private fun processState(viewState: ViewState<List<RowDataModel<FriendsListType, *>>>) {
        pb_friends_list_loading.setVisible(viewState.isLoading())
        ll_friends_list_error_wrapper.setVisible(viewState.isError())

        if (viewState.isSuccess()) {
            setData(viewState.data)
        }

        if (viewState.isError()) {
            Toast.makeText(this, viewState.error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData(data: List<RowDataModel<FriendsListType, *>>?) {
        data?.let {
            friendsAdapter.reload(data)
        }
    }
}

package com.lab422.vkanalyzer.ui.friendsList

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.friendsList.adapter.FriendsListAdapter
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendViewHolder
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.utils.extensions.gone
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.common.StringProvider
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isError
import com.lab422.common.viewState.isLoading
import com.lab422.common.viewState.isSuccess
import kotlinx.android.synthetic.main.activity_friends_list.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel

class FriendsListActivity : BaseActivity(R.layout.activity_friends_list), FriendViewHolder.Listener,
    SearchView.OnQueryTextListener {

    private lateinit var viewModel: FriendsListViewModel
    private val stringProvider: StringProvider = get()
    private var activityLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    private var friendsAdapter: FriendsListAdapter
    private lateinit var searchItem: SearchView

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, FriendsListActivity()::class.java)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_friends_list, menu)
        val menuItem: MenuItem = menu.findItem(R.id.menu_search)
        searchItem = menuItem.actionView as SearchView
        searchItem.setOnQueryTextListener(this)

        return true
    }

    override fun onBackPressed() {
        if (searchItem.isIconified.not()) {
            searchItem.isIconified = true
            return
        }

        super.onBackPressed()
    }

    override fun onQueryTextSubmit(query: String): Boolean = true

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.onSearchQueryTyped(newText)
        return true
    }

    override fun onFriendClicked(id: Long, name: String) {
        setResult(Activity.RESULT_OK, Intent().putExtra(FRIEND_ID_KEY, FriendModel(id, name)))
        finish()
    }

    private fun initViews() {
        ll_friends_list_error_wrapper.gone()
        btn_friends_list_return.setOnClickListener { finish() }

        rv_friends_list.run {
            layoutManager = activityLayoutManager
            adapter = friendsAdapter
        }
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

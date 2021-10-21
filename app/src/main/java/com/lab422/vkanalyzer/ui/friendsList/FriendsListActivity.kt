package com.lab422.vkanalyzer.ui.friendsList

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab422.common.StringProvider
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isError
import com.lab422.common.viewState.isLoading
import com.lab422.common.viewState.isSuccess
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.ActivityFriendsListBinding
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.friendsList.adapter.FriendsListAdapter
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendViewHolder
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.utils.extensions.gone
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel

class FriendsListActivity :
    BaseActivity(),
    FriendViewHolder.Listener,
    SearchView.OnQueryTextListener {

    private lateinit var viewModel: FriendsListViewModel
    private val stringProvider: StringProvider = get()
    private val imageLoader: ImageLoader = get()
    private var activityLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    private var friendsAdapter = FriendsListAdapter(listOf(), stringProvider, this, this, imageLoader)
    private lateinit var searchItem: SearchView

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, FriendsListActivity()::class.java)
        const val FRIEND_ID_KEY = "friend_id_key"
    }

    override fun getToolBarViewId(): Toolbar = binding.toolbarFriendsList

    override val toolbarName: Int = R.string.friends_list

    private lateinit var binding: ActivityFriendsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        with(binding) {
            llFriendsListErrorWrapper.gone()
            btnFriendsListReturn.setOnClickListener { finish() }

            rvFriendsList.run {
                layoutManager = activityLayoutManager
                adapter = friendsAdapter
            }
        }
    }

    private fun initObservers() {
        viewModel.state.observe(this, { viewState -> processState(viewState) })
    }

    private fun processState(viewState: ViewState<List<RowDataModel<FriendsListType, *>>>) {
        with(binding) {
            pbFriendsListLoading.setVisible(viewState.isLoading())
            llFriendsListErrorWrapper.setVisible(viewState.isError())
        }

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

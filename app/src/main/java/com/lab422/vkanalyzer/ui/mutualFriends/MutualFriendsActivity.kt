package com.lab422.vkanalyzer.ui.mutualFriends

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.ui.base.BaseItemDecoration
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendViewHolder
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.MutualFriendsListAdapter
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.utils.extensions.hide
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.stringProvider.StringProvider
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.lab422.vkanalyzer.utils.viewState.isError
import com.lab422.vkanalyzer.utils.viewState.isLoading
import com.lab422.vkanalyzer.utils.viewState.isSuccess
import kotlinx.android.synthetic.main.activity_mutual_friends.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf



class MutualFriendsActivity : BaseActivity(R.layout.activity_mutual_friends), FriendViewHolder.Listener,
    SearchView.OnQueryTextListener {

    private lateinit var viewModel: MutualViewModel

    private val stringProvider: StringProvider = get()
    private var activityLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    private var friendsAdapter: MutualFriendsListAdapter
    private lateinit var searchItem: SearchView

    companion object {
        private const val mutualModelKey = "mutualModelKey"
        fun createIntent(context: Context, firstId: String, secondId: String): Intent {
            val intent = Intent(context, MutualFriendsActivity()::class.java)
            intent.putExtra(mutualModelKey, MutualFriendsModel(firstId, secondId))
            return intent
        }
    }

    init {
        friendsAdapter = MutualFriendsListAdapter(listOf(), stringProvider, this, this)
    }

    override fun getToolBarViewId(): Int = R.id.toolbar_mutual_friends_list

    override val toolbarName: Int = R.string.mutual_friends_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar()
        viewModel = getViewModel {
            parametersOf(intent.extras?.getParcelable<MutualFriendsModel>(mutualModelKey))
        }
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

    override fun onFriendClicked(id: Long) {
        val link = String.format("https://vk.com/id%d", id)

        val eventProperties = JSONObject()
        try {
            eventProperties.put("opened_user_link", link)
        } catch (exception: JSONException) {
        }
        Amplitude.getInstance().logEvent("open by link", eventProperties);

        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    override fun onQueryTextSubmit(query: String): Boolean = true

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.onSearchQueryTyped(newText)
        return true
    }

    override fun onBackPressed() {
        if (searchItem.isIconified.not()) {
            searchItem.isIconified = true
            return
        }

        super.onBackPressed()
    }

    private fun initViews() {
        ll_mutual_friends_error_wrapper.hide()
        btn_return.setOnClickListener { onSupportNavigateUp() }

        rv_mutual_friends.run {
            layoutManager = activityLayoutManager
            adapter = friendsAdapter
        }
        val divider = BaseItemDecoration(this)
        rv_mutual_friends.addItemDecoration(divider, 0)
    }

    private fun initObservers() {
        viewModel.getState().observe(this, Observer { viewState ->
            processState(viewState)
        })
    }

    private fun processState(viewState: ViewState<List<RowDataModel<FriendsListType, *>>>) {
        pb_mutual_friends_loading.setVisible(viewState.isLoading())
        ll_mutual_friends_error_wrapper.setVisible(viewState.isError())

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

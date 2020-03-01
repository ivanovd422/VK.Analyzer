package com.lab422.vkanalyzer.ui.mutualFriends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.MutualFriendsAdapter
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.MutualFriendsType
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.utils.extensions.hide
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.stringProvider.StringProvider
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.lab422.vkanalyzer.utils.viewState.isError
import com.lab422.vkanalyzer.utils.viewState.isLoading
import com.lab422.vkanalyzer.utils.viewState.isSuccess
import kotlinx.android.synthetic.main.activity_mutual_friends.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class MutualFriendsActivity : AppCompatActivity(R.layout.activity_mutual_friends) {

    private lateinit var viewModel: MutualViewModel

    private val stringProvider: StringProvider = get()
    private lateinit var activityLayoutManager: LinearLayoutManager
    //todo rewrite adapter with LiveData inside
    private lateinit var friendsAdapter: MutualFriendsAdapter

    companion object {
        private const val mutualModelKey = "mutualModelKey"
        fun createIntent(context: Context, firstId: Long, secondId: Long): Intent {
            val intent = Intent(context, MutualFriendsActivity()::class.java)
            intent.putExtra(mutualModelKey, MutualFriendsModel(firstId, secondId))
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel {
            parametersOf(intent.extras?.getParcelable<MutualFriendsModel>(mutualModelKey))
        }
        initViews()
        initObservers()
    }

    private fun initViews() {
        ll_mutual_friends_error_wrapper.hide()
        btn_retry_fetch_friends.setOnClickListener { viewModel.retryLoading() }
        btn_return.setOnClickListener { finishAffinity() }
    }

    private fun initObservers() {
        viewModel.getState().observe(this, Observer { viewState ->
            processState(viewState)
        })
    }

    private fun processState(viewState: ViewState<List<RowDataModel<MutualFriendsType, *>>>) {
        pb_mutual_friends_loading.setVisible(viewState.isLoading())
        ll_mutual_friends_error_wrapper.setVisible(viewState.isError())

        if (viewState.isSuccess()) {
            setData(viewState.data)
        }
    }

    private fun setData(data: List<RowDataModel<MutualFriendsType, *>>?) {
        data?.let {
            activityLayoutManager = LinearLayoutManager(this)
            friendsAdapter = MutualFriendsAdapter(it, stringProvider)
            rv_mutual_friends.run {
                layoutManager = activityLayoutManager
                adapter = friendsAdapter
            }
        }
    }
}

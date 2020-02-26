package com.lab422.vkanalyzer.ui.mutualFriends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import kotlinx.android.synthetic.main.activity_mutual_friends.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MutualFriendsActivity : AppCompatActivity(R.layout.activity_mutual_friends) {

    private lateinit var viewModel: MutualViewModel

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
        val model = intent.extras?.getParcelable<MutualFriendsModel>(mutualModelKey)

        tv_first_user_id.text = model?.firstId.toString()
        tv_second_user_id.text = model?.secondId.toString()

        // initViews()
    }
}

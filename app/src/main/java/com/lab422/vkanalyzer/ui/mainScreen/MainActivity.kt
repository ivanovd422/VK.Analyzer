package com.lab422.vkanalyzer.ui.mainScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.extensions.attach
import com.lab422.vkanalyzer.utils.extensions.detach
import com.lab422.vkanalyzer.utils.extensions.setActive
import com.lab422.vkanalyzer.utils.navigator.Navigator
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var viewModel: MainViewModel
    private val navigator: Navigator = get()
    private val tracker: TrackerService by inject()

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity()::class.java)
        }
    }

    override fun getToolBarViewId(): Int = R.id.main_activity_bar_toolbar

    override val toolbarName: Int = R.string.main_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        setToolBar(false)

        initObservables()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initObservables() {
        viewModel.getBottomBarItems().observe(this, Observer {
            initNavigationBar(it)
        })

        viewModel.getCurrentTab().observe(this, Observer {
            selectTab(it)
        })
    }

    private fun initNavigationBar(barItems: List<BarItem>) {
        bottom_navigation.apply {
            for (barItem in barItems) {
                menu.add(Menu.NONE, barItem.id, Menu.NONE, barItem.itemTitle).setIcon(barItem.icon)
            }
            setOnNavigationItemSelectedListener {
                viewModel.onBottomMenuClicked(it.itemId)
                true
            }
        }
    }

    private fun selectTab(item: BarItem) {
        val fragment = supportFragmentManager.findFragmentByTag(item.fragmentTag)
            ?: item.command.execute(this) as Fragment

        fragment.let {
            if (it.isAdded) return@let
            supportFragmentManager.detach(R.id.main_activity_bar_container)
            supportFragmentManager.attach(it, item.fragmentTag, R.id.main_activity_bar_container)
            supportFragmentManager.executePendingTransactions()
        }

        bottom_navigation.setActive(item.id)

        item.toolbarTitle?.let { setToolbar(it) }
    }

    private fun setToolbar(toolbarText: Int) {
        supportActionBar?.title = getString(toolbarText)
    }
}

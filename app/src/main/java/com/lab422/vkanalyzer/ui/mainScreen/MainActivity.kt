package com.lab422.vkanalyzer.ui.mainScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.ActivityMainBinding
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.utils.extensions.attach
import com.lab422.vkanalyzer.utils.extensions.detach
import com.lab422.vkanalyzer.utils.extensions.setActive
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity()::class.java)
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun getToolBarViewId(): Toolbar = binding.mainActivityBarToolbar.toolbarDefault

    override val toolbarName: Int = R.string.main_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = getViewModel()
        setToolBar(false)

        initObservables()
    }

    private fun initObservables() {
        viewModel.getBottomBarItems().observe(
            this,
            {
                initNavigationBar(it)
            }
        )

        viewModel.getCurrentTab().observe(
            this,
            {
                selectTab(it)
            }
        )
    }

    private fun initNavigationBar(barItems: List<BarItem>) {
        binding.bottomNavigation.apply {
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

        binding.bottomNavigation.setActive(item.id)

        item.toolbarTitle?.let { setToolbar(it) }
    }

    private fun setToolbar(toolbarText: Int) {
        supportActionBar?.title = getString(toolbarText)
    }
}

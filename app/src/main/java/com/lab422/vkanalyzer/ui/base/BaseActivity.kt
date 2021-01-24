package com.lab422.vkanalyzer.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lab422.vkanalyzer.utils.extensions.overridePendingTransitionAsSlideOut

abstract class BaseActivity(@LayoutRes layout: Int) : AppCompatActivity(layout) {

    protected val toolbar: Toolbar by lazy { findViewById<Toolbar>(getToolBarViewId()) }

    protected abstract fun getToolBarViewId(): Int

    protected abstract val toolbarName: Int

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overrideBackTransition()
    }

    open fun overrideBackTransition() {
        overridePendingTransitionAsSlideOut()
    }

    fun clickBackArrowIcon(f: () -> Unit): Boolean {
        f()
        return true
    }

    fun setToolBar(showHomeBtn: Boolean = true) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(showHomeBtn)
        supportActionBar?.setHomeButtonEnabled(showHomeBtn)
        supportActionBar?.title = getString(toolbarName)
    }
}

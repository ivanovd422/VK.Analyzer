package com.lab422.vkanalyzer.ui.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.onboarding.adapter.OnBoardingAdapter
import com.lab422.vkanalyzer.utils.extensions.getScreenWidth
import kotlinx.android.synthetic.main.activity_on_boarding.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class OnBoardingActivity : AppCompatActivity(R.layout.activity_on_boarding) {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, OnBoardingActivity()::class.java)
        }
    }

    private var pagerAdapter: OnBoardingAdapter
    private val viewModel: OnBoardingViewModel by inject()

    private val stringProvider: StringProvider = get()

    init {
        pagerAdapter = OnBoardingAdapter(listOf(), stringProvider, this/*, this*/)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setScreenWidth(getScreenWidth())
        initViews()
        initObservers()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        rv_on_boarding.run {
            layoutManager = LinearLayoutManager(this@OnBoardingActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = pagerAdapter
            setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.onPauseStory(event.rawX)
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        viewModel.onResumeStory()
                    }
                }

                true
            }
        }

        viewModel.onBoardingImages.observe(this, Observer { data ->
            data?.let {
                pagerAdapter.reload(data)
            }
        })
    }

    private fun initObservers() {
        viewModel.onBoardingPositionEvent.observe(this, Observer {
            rv_on_boarding.scrollToPosition(it)
        })
    }
}
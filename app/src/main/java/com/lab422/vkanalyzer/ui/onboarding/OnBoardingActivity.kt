package com.lab422.vkanalyzer.ui.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.databinding.ActivityOnBoardingBinding
import com.lab422.vkanalyzer.ui.onboarding.adapter.OnBoardingAdapter
import com.lab422.vkanalyzer.utils.extensions.getScreenWidth
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader
import com.lab422.vkanalyzer.utils.view.InstaStorySlider
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class OnBoardingActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, OnBoardingActivity()::class.java)
        }
    }

    private var pagerAdapter: OnBoardingAdapter
    private val viewModel: OnBoardingViewModel by inject()

    private val stringProvider: StringProvider = get()
    private val imageLoader: ImageLoader = get()

    private lateinit var binding: ActivityOnBoardingBinding

    init {
        pagerAdapter = OnBoardingAdapter(listOf(), stringProvider, this, imageLoader)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.setScreenWidth(getScreenWidth())
        initViews()
        initObservers()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        with(binding) {
            rvOnBoarding.run {
                layoutManager = LinearLayoutManager(this@OnBoardingActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = pagerAdapter
                setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            viewModel.onPauseStory(event.rawX)
                            viewStoriesSlider.pauseStory()
                        }
                        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                            viewModel.onResumeStory()
                            viewStoriesSlider.resumeStory()
                        }
                    }

                    true
                }
            }

            icClose.setOnClickListener {
                viewModel.onCloseOnBoardingClicked()
            }

            viewStoriesSlider.setStoryListener(object : InstaStorySlider.Listener {
                override fun onStoryEnd(isLastStory: Boolean) {
                    viewModel.onStoryEnd(isLastStory)
                }
            })
        }
    }

    private fun initObservers() {
        viewModel.onBoardingPositionEvent.observe(
            this,
            {
                binding.rvOnBoarding.scrollToPosition(it)
            }
        )

        viewModel.onBoardingImages.observe(
            this,
            { data ->
                data?.let {
                    pagerAdapter.reload(data)
                }
            }
        )

        viewModel.onScrollForwardDirection.observe(
            this,
            { isForwardDirection ->
                if (isForwardDirection) {
                    binding.viewStoriesSlider.scrollToNextStory()
                } else {
                    binding.viewStoriesSlider.scrollToPreviousStory()
                }
            }
        )
    }
}

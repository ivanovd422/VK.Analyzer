package com.lab422.vkanalyzer.utils.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.utils.extensions.dpToPx

class InstaStorySlider : View {

    private companion object {
        const val STORY_LINE_INTERVAL = 8f
        const val SIDE_INTERVAL = 12f
        const val STORY_ANIMATION_DURATION = 5000L
        const val STORIES_COUNT = 3
    }

    private val bgColor = ContextCompat.getColor(context, R.color.colorAlto)
    private val frontColor = ContextCompat.getColor(context, R.color.colorSilver)

    private val storyLineIntervalInPx = context.dpToPx(STORY_LINE_INTERVAL.toInt())
    private val sideIntervalInPx = context.dpToPx(SIDE_INTERVAL.toInt())

    private val bgPaint: Paint = Paint().apply {
        color = bgColor
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 10f
    }

    private val frontPaint: Paint = Paint().apply {
        color = frontColor
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 10f
    }

    // pair of start and end X coordinates
    private var storyPoints: List<Pair<Float, Float>>? = null
    private var storyAnimation: ValueAnimator? = null
    private var lineOffset: Float = SIDE_INTERVAL
    private var currentStoryAnimationNumber = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (storyPoints.isNullOrEmpty()) {
            storyPoints = calculateStoryPoints(STORIES_COUNT)
        }

        // draw bg line
        drawStaticBackgroundLine(canvas)

        val animationPoints = calculateAnimatedStoryPoints()
        drawFrontAnimation(canvas, animationPoints.first, lineOffset)

        // start animation if not started yet
        if (storyAnimation == null) {
            startLineAnimation(animationPoints.first, animationPoints.second)
        }
    }

    override fun onDetachedFromWindow() {
        storyAnimation?.cancel()
        super.onDetachedFromWindow()
    }

    private fun drawStaticBackgroundLine(canvas: Canvas) {
        storyPoints?.forEachIndexed { index, points ->
            val paint = if (index < currentStoryAnimationNumber) frontPaint else bgPaint
            drawLine(canvas, points.first, points.second, paint)
        }
    }

    private fun drawLine(canvas: Canvas, startPoint: Float, endPoint: Float, paint: Paint) {
        canvas.drawRoundRect(RectF(startPoint, 20f, endPoint, 24f), 40f, 40f, paint);
    }

    private fun calculateStoryPoints(count: Int): List<Pair<Float, Float>> {
        val storyPoints = mutableListOf<Pair<Float, Float>>()
        val storyLineWidth = (width - ((sideIntervalInPx * 2) + (storyLineIntervalInPx * (count - 1)))) / count
        var cursor = 0f

        for (x in 0 until count) {
            val startPoint: Float
            val endPoint: Float

            // the first interval is different from rest.
            if (x == 0) {
                cursor += sideIntervalInPx
                startPoint = cursor

                cursor += storyLineWidth
                endPoint = cursor

                storyPoints.add(Pair(startPoint, endPoint))

                continue
            }

            cursor += storyLineIntervalInPx
            startPoint = cursor

            cursor += storyLineWidth
            endPoint = cursor

            storyPoints.add(Pair(startPoint, endPoint))
        }

        return storyPoints
    }

    private fun calculateAnimatedStoryPoints(): Pair<Float, Float> {
        return storyPoints?.get(currentStoryAnimationNumber)!!
    }

    private fun startLineAnimation(startPosition: Float, finalOffset: Float) {
        storyAnimation = ValueAnimator.ofFloat(startPosition, finalOffset).apply {
            addUpdateListener {
                lineOffset = it.animatedValue as Float
                postInvalidate()
            }
            doOnEnd {
                onStoryAnimationEnd()
            }
            duration = STORY_ANIMATION_DURATION
            interpolator = LinearInterpolator()
            start()
        }
    }

    private fun drawFrontAnimation(canvas: Canvas, startLinePoint: Float, currentAnimatedPosition: Float) {
        drawLine(canvas, startLinePoint, currentAnimatedPosition, frontPaint)
    }

    private fun onStoryAnimationEnd() {
        if (currentStoryAnimationNumber == STORIES_COUNT - 1) {
            Log.d("myTag", "Animation has finished")
            return
        }
        currentStoryAnimationNumber++

        storyAnimation?.cancel()
        storyAnimation = null
        invalidate()
    }
}
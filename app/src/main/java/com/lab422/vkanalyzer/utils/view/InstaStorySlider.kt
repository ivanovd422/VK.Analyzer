package com.lab422.vkanalyzer.utils.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.utils.extensions.dpToPx

class InstaStorySlider : View {

    private companion object {
        const val STORY_LINE_INTERVAL = 8f
        const val SIDE_INTERVAL = 12f
    }

    private val bgColor = ContextCompat.getColor(context, R.color.blackAlpha10)
    private val frontColor = ContextCompat.getColor(context, R.color.blackAlpha20)

    private val storyLineIntervalInPx = context.dpToPx(STORY_LINE_INTERVAL.toInt())
    private val sideIntervalInPx = context.dpToPx(SIDE_INTERVAL.toInt())

    private val bgLine: Paint = Paint().apply {
        color = bgColor
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 10f
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        calculateStoryPoints(3).forEach { points ->
            drawLine(canvas, points.first, points.second)
        }

    }

    private fun drawLine(canvas: Canvas, startPoint: Float, endPoint: Float) {
        canvas.drawRoundRect(RectF(startPoint, 20f, endPoint, 24f), 40f, 40f, bgLine);
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
}


/*
val scaleAnim = ObjectAnimator.ofFloat(view_stories_slider, "translationX", 100f)
scaleAnim.duration = 3000
// scaleAnim.repeatCount = ValueAnimator.INFINITE
// scaleAnim.repeatMode = ValueAnimator.REVERSE
scaleAnim.start()*/

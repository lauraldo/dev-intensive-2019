package ru.skillbranch.devintensive.ui.custom

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.ColorInt
import kotlin.math.min

class TextDrawable(private val text: String, @ColorInt private val bgColor: Int): ShapeDrawable() {

    private val textPaint = Paint()
    private val bgPaint = Paint()

    private var width = DEFAULT_WIDTH
    private var height = DEFAULT_HEIGHT

    init {
        with(textPaint) {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }
        with(bgPaint) {
            color = bgColor
            style = Paint.Style.FILL
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val w = if (bounds.width() == 0) width else bounds.width()
        val h = if (bounds.height() == 0) height else bounds.height()
        width = w
        height = h
        val r = RectF(bounds.left.toFloat(), bounds.top.toFloat(), w.toFloat(), h.toFloat())
        canvas.drawOval(r, bgPaint)
        val count = canvas.save()
        canvas.translate(r.left, r.top)
        textPaint.textSize = min(r.width(), r.height()) / 2
        canvas.drawText(text, r.width() / 2,
            r.height() / 2 - ((textPaint.descent() + textPaint.ascent()) / 2),
            textPaint)
        canvas.restoreToCount(count)
    }

    override fun getIntrinsicWidth(): Int {
        return width
    }

    override fun getIntrinsicHeight(): Int {
        return height
    }

    companion object {
        const val DEFAULT_WIDTH = 120
        const val DEFAULT_HEIGHT = 120
    }

}
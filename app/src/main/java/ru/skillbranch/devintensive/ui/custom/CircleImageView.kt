package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatImageView
import ru.skillbranch.devintensive.R
import kotlin.math.min


/*Реализуй CustomView с названием класса CircleImageView и кастомными xml атрибутами
cv_borderColor (цвет границы (format="color") по умолчанию white) и
cv_borderWidth (ширина границы (format="dimension") по умолчанию 2dp).
CircleImageView должна превращать установленное изображение в круглое изображение с цветной рамкой,
у CircleImageView должны быть реализованы методы
@Dimension getBorderWidth():Int, setBorderWidth(@Dimension dp:Int),
getBorderColor():Int, setBorderColor(hex:String), setBorderColor(@ColorRes colorId: Int).
Используй CircleImageView как ImageView для аватара пользователя (@id/iv_avatar)*/

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatImageView(context, attrs, defStyleAttr) {

    private var bitmap: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var bitmapWidth = 0
    private var bitmapHeight = 0
    private val bitmapPaint = Paint()
    private val borderPaint = Paint()
    private val drawableRect = RectF()
    private val borderRect = RectF()
    private var drawableRadius = 0f
    private var borderRadius = 0f

    private val defaultBorderWidthPx: Int by lazy {
        (context.resources.displayMetrics.density * DEFAULT_BORDER_WIDTH_DP).toInt()
    }

    private var borderWidth = defaultBorderWidthPx

    private var borderColor = DEFAULT_BORDER_COLOR

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, defaultBorderWidthPx)
            a.recycle()
        }
    }

    private fun setup() {

        fun calculateBounds(): RectF {
            val availableWidth = width - paddingLeft - paddingRight
            val availableHeight = height - paddingTop - paddingBottom

            val sideLen = min(availableWidth, availableHeight)

            val left = paddingLeft + (availableWidth - sideLen) / 2f
            val top = paddingTop + (availableHeight - sideLen) / 2f

            return RectF(left, top, left + sideLen, top + sideLen)
        }

        fun updateShaderMatrix() {
            val shaderMatrix = Matrix()

            var scale = 0f
            var dx = 0f
            var dy = 0f

            shaderMatrix.set(null)

            with(drawableRect) {
                if (bitmapWidth * height() > width() * bitmapHeight) {
                    scale = height() / bitmapHeight
                    dx = (width() - bitmapWidth * scale) * 0.5f
                } else {
                    scale = width() / bitmapWidth
                    dy = (height() - bitmapHeight * scale) * 0.5f
                }

                shaderMatrix.setScale(scale, scale)
                shaderMatrix.postTranslate((dx + 0.5f).toInt() + left, (dy + 0.5f).toInt() + top)
            }

            bitmapShader?.setLocalMatrix(shaderMatrix)
        }

        if (width == 0 && height == 0) return
        bitmap?.run {
            bitmapWidth = width
            bitmapHeight = height
            bitmapShader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            with(bitmapPaint) {
                isAntiAlias = true
                isDither = true
                isFilterBitmap = true
                shader = bitmapShader
            }
        }

        with(borderPaint) {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = borderColor
            strokeWidth = borderWidth.toFloat()
        }

        with(borderRect) {
            set(calculateBounds())
            borderRadius = min((height() - borderWidth) / 2f, (width() - borderWidth) / 2f)
        }

        with(drawableRect) {
            set(borderRect)
            drawableRadius = min(height() / 2f, width() / 2f)
        }

        updateShaderMatrix()
        invalidate()
    }

    private fun initBitmap() {

        fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
            if (drawable == null) {
                return null
            }
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            return try {
                val bmp: Bitmap = if (drawable is ColorDrawable) {
                    Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
                } else {
                    Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
                }

                val canvas = Canvas(bmp)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bmp
            } catch (e: Exception){
                null
            }
        }

        bitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        initBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initBitmap()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        initBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initBitmap()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        val color = context.resources.getColor(colorId, context.theme)
        if (borderColor == color) {
            return
        }
        borderColor = color
        borderPaint.color = borderColor
        invalidate()
    }

    fun setBorderColor(hex: String) {
        val color = Color.parseColor(hex)
        if (borderColor == color) {
             return
        }
        borderColor = color
        borderPaint.color = borderColor
        invalidate()
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderWidth(@Dimension(unit = Dimension.DP) width: Int) {
        val widthPx = (context.resources.displayMetrics.density * width).toInt()
        if (widthPx == borderWidth) {
            return
        }
        borderWidth = widthPx
        setup()
    }

    @Dimension(unit = Dimension.DP)
    fun getBorderWidth(): Int {
        return (borderWidth / context.resources.displayMetrics.density).toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        if (bitmap == null) {
            return
        }
        canvas?.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, bitmapPaint)
        if (borderWidth > 0) {
            canvas?.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, borderPaint)
        }
    }

    companion object {

        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 2

        @Dimension(unit = Dimension.DP)
        const val DEFAULT_BORDER_WIDTH_DP = 2

        @ColorInt
        const val DEFAULT_BORDER_COLOR = Color.WHITE
    }
}
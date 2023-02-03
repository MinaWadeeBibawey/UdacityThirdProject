package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var mainButtonColor = 0
    private var loadingButtonColor = 0
    private var ovalColor = 0
    private var progress = 0
    private var rectF: RectF

    private val path = Path()
    private val circleRadius = resources. getDimension (R.dimen.circleRadius)

    private val valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(2000).apply {
        interpolator = AccelerateDecelerateInterpolator()
        repeatCount = ValueAnimator.INFINITE

        addUpdateListener {
            progress = it.animatedValue as Int
            invalidate()
        }
    }


    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Loading -> {
                valueAnimator.start()
            }
            else -> {
                valueAnimator.cancel()
                progress = 0
                invalidate()
            }
        }

    }

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            mainButtonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            loadingButtonColor = getColor(R.styleable.LoadingButton_loadingButtonColor, 0)
            ovalColor = getColor(R.styleable.LoadingButton_ovalColor, 0)
        }

        rectF = RectF(70F, 50F, 120F, 100F)
    }

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        isAntiAlias = true
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        buttonState = ButtonState.Clicked
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawRect(canvas)
        drawLoadingRect(canvas)
        drawText(canvas)
        drawArc(canvas)

    }

    private fun drawRect(canvas: Canvas) {
        paint.color = mainButtonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
    }

    private fun drawLoadingRect(canvas: Canvas) {
        paint.color = loadingButtonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat() * progress / 150, heightSize.toFloat(), paint)
    }

    private fun drawText(canvas: Canvas) {
        paint.color = Color.WHITE
        canvas.drawText(
            context.getString(buttonState.lable),
            widthSize.toFloat() / 2,
            heightSize.toFloat() / 1.5f,
            paint
        )
    }

    private fun drawArc(canvas: Canvas) {
        paint.color = ovalColor
        canvas.drawArc(rectF, 0F, widthSize.toFloat() * progress / 360, true, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun loadingButtonState(state: Int) {
        when (state) {
            0 -> {
                buttonState = ButtonState.Loading
            }
            1 -> {
                buttonState = ButtonState.Completed
            }
        }
    }

}
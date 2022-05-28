package com.devpark.recoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SoundVisualizerView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    //
    var onRequestCurrentAmplitude: (() -> Int)? = null

    //모서리가 각지지 않게 처리 하기 위해 ANTI_ALIAS_FLAG 처리
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private var drawingWidht: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()

    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0

    private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            if (!isReplaying) {
                //MainActivity에  onRequestCurrentAmplitude 호출 함수 인자를 받아 온다
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                //마지막으로 들어온 즉 최신 소리높낮이(Amplitudes)가 제일 먼저 그려져야 한다.
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }


            // 새로추가 된 뷰를 drawing 갱신
            invalidate()

            // 0.2초 후에 계속 실행 시키기
            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidht = w
        drawingHeight = h
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return

        //뷰의 y의 값의 센터를 결정
        val centerY = drawingHeight / 2f
        //시작 포인트
        var offsetX = drawingWidht.toFloat()

        drawingAmplitudes.let { amplitudes ->
            if (isReplaying) {
                //리플레잉일 경우 가장 뒤에 있는 것부터 순차적으로 가져온다.
                amplitudes.takeLast(replayingPosition)
            } else {
                amplitudes
            }
        }.forEach { amplitue ->
            val lineLength = amplitue / MAX_AMPLITUDE * drawingHeight * 0.8F

            offsetX -= LINE_SPACE

            // 화면 왼쪽을 벗어나면 더 이상 그려주지 않기
            if (offsetX < 0) return@forEach

            canvas.drawLine(
                offsetX,
                centerY - lineLength / 2F,
                offsetX,
                centerY + lineLength / 2F,
                amplitudePaint
            )

        }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing() {
        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualization() {
        drawingAmplitudes = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }


}
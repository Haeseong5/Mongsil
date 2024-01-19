package com.cashproject.mongsil.extension

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.startFakeDrag(
    duration: Long = 1000L,
    direction: Direction,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),    // 처음엔 빠르게 끝날 때는 천천히 가속시키는 interpolator
    pxToMove: Int = 300, // 이동할 px 거리,
) {
    val pxToDrag: Int = if (direction == Direction.START) pxToMove else -pxToMove
    val animator = ValueAnimator.ofInt(0, -pxToDrag)
    var previousValue = 0

    animator.addUpdateListener { valueAnimator ->
        if (valueAnimator.animatedValue == -pxToDrag) endFakeDrag()

        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()

        fakeDragBy(-currentPxToDrag)    //  좌우 방향으로 드래그. fakeDrag중인 경우 true 반환, fake Drag가 아닌순간 false반환
        previousValue = currentValue
    }

    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            beginFakeDrag()
        }

        override fun onAnimationEnd(animation: Animator) {
            endFakeDrag()
        }

        override fun onAnimationCancel(animation: Animator) { /* Ignored */
        }

        override fun onAnimationRepeat(animation: Animator) { /* Ignored */
        }
    })

    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}

enum class Direction {
    START, END
}
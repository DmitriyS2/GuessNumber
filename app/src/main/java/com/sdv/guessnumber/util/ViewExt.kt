package com.sdv.guessnumber.util

import android.view.animation.DecelerateInterpolator
import android.widget.TextView

fun TextView.scale() {
    this.scaleX = 0f
    this.scaleY = 0f
    this.animate()
        .scaleX(1f)
        .scaleY(1f)
        .setDuration(500)
        .setInterpolator(DecelerateInterpolator())
        .start()
}
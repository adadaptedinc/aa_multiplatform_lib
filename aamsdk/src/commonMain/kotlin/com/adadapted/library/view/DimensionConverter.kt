package com.adadapted.library.view

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object DimensionConverter {
    private var scale: Float = 0f

    fun createInstance(scale: Float) {
        this.scale = scale
    }

    fun convertDpToPx(dpValue: Int): Int {
        return if (dpValue > 0) {
            (dpValue * scale + 0.5f).toInt()
        } else dpValue
    }
}

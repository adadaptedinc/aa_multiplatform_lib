package com.adadapted.library.dimension

import kotlin.native.concurrent.ThreadLocal

class DimensionConverter(private val scale: Float) {
    fun convertDpToPx(dpValue: Int): Int {
        return if (dpValue > 0) {
            (dpValue * scale + 0.5f).toInt()
        } else dpValue
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: DimensionConverter

        fun getInstance(): DimensionConverter {
            return if(this::instance.isInitialized) {
                instance
            } else {
                createInstance(0f)
                instance
            }
        }

        fun createInstance(scale: Float) {
            instance = DimensionConverter(scale)
        }
    }
}
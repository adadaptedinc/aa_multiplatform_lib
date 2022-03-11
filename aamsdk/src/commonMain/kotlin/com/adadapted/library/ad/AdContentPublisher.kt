package com.adadapted.library.ad

import com.adadapted.library.concurrency.Transporter
import kotlin.native.concurrent.ThreadLocal

class AdContentPublisher private constructor(private val transporter: Transporter) {
    private val listeners: MutableSet<AdContentListener> = HashSet()

    fun addListener(listener: AdContentListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: AdContentListener) {
        listeners.remove(listener)
    }

    fun publishContent(zoneId: String, content: AdContent) {
        if (content.hasNoItems()) {
            return
        }
        transporter.dispatchToMain {
        for (listener in listeners) {
                listener.onContentAvailable(zoneId, content)
            }
        }
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: AdContentPublisher

        fun getInstance(transporter: Transporter = Transporter()): AdContentPublisher {
            if (!this::instance.isInitialized) {
                instance = AdContentPublisher(transporter)
            }
            return instance
        }
    }
}
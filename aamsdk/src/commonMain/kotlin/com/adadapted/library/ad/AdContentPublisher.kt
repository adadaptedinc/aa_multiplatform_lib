package com.adadapted.library.ad

import com.adadapted.library.concurrency.Transporter
import kotlin.native.concurrent.ThreadLocal

class AdContentPublisher private constructor() {
    private val listeners: MutableSet<AdContentListener> = HashSet()
    private val transporter = Transporter() //todo inject

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
        transporter.dispatchToBackground {
            for (listener in listeners) {
                listener.onContentAvailable(zoneId, content)
            }
        }
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: AdContentPublisher

        fun getInstance(): AdContentPublisher {
            if (!this::instance.isInitialized) {
                createInstance()
            }
            return instance
        }

        fun createInstance() {
            instance = AdContentPublisher()
        }
    }
}
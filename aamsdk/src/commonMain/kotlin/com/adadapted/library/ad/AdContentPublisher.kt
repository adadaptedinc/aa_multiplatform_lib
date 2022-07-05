package com.adadapted.library.ad

import com.adadapted.library.concurrency.Transporter
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object AdContentPublisher {

    private var transporter: Transporter = Transporter()
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
}
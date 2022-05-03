package com.adadapted.library.event

import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.concurrency.TransporterCoroutineScope
import kotlin.native.concurrent.ThreadLocal

class EventPublisher private constructor(private val transporter: TransporterCoroutineScope) :
    EventClient.Listener {

    private lateinit var listener: (zoneId: String, eventType: String) -> Unit

    fun setListener(listener: (zoneId: String, eventType: String) -> Unit) {
        this.listener = listener
    }

    override fun onAdEventTracked(event: AdEvent?) {
        if (!::listener.isInitialized || event == null) {
            return
        }
        transporter.dispatchToBackground { listener.invoke(event.zoneId, event.eventType) }
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: EventPublisher

        fun getInstance(transporter: Transporter = Transporter()): EventPublisher {
            if (!this::instance.isInitialized) {
                instance = EventPublisher(transporter)
            }
            return instance
        }
    }

    init {
        EventClient.getInstance().addListener(this)
    }
}
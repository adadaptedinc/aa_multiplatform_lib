package com.adadapted.library.event

import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.interfaces.EventClientListener
import kotlin.native.concurrent.ThreadLocal

class EventBroadcaster private constructor(private val transporter: TransporterCoroutineScope) :
    EventClientListener {

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
        private lateinit var instance: EventBroadcaster

        fun getInstance(transporter: Transporter = Transporter()): EventBroadcaster {
            if (!this::instance.isInitialized) {
                instance = EventBroadcaster(transporter)
            }
            return instance
        }
    }

    init {
        EventClient.addListener(this)
    }
}
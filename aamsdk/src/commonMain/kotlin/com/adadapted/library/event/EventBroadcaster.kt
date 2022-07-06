package com.adadapted.library.event

import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.interfaces.EventBroadcastListener
import com.adadapted.library.interfaces.EventClientListener
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object EventBroadcaster : EventClientListener {

    private val transporter: TransporterCoroutineScope = Transporter()
    private var listener: EventBroadcastListener? = null

    fun setListener(listener: EventBroadcastListener) {
        this.listener = listener
    }

    override fun onAdEventTracked(event: AdEvent?) {
        if (listener == null || event == null) {
            return
        }
        transporter.dispatchToThread { listener?.onAdEventTracked(event.zoneId, event.eventType) }
    }

    init {
        EventClient.addListener(this)
    }
}
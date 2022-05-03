package com.adadapted.library.event

import com.adadapted.library.ad.Ad
import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionClient
import com.adadapted.library.session.SessionListener
import kotlin.jvm.Synchronized
import kotlin.native.concurrent.ThreadLocal

class EventClient private constructor(private val eventAdapter: EventAdapter, private val transporter: TransporterCoroutineScope
) : SessionListener {
    interface Listener {
        fun onAdEventTracked(event: AdEvent?)
    }

    private val listeners: MutableSet<Listener>
    private val events: MutableSet<AdEvent>
    private var session: Session? = null

    @Synchronized
    private fun fileEvent(ad: Ad, eventType: String) {
        if (session == null) {
            return
        }
        val event = AdEvent(
            ad.id,
            ad.zoneId,
            ad.impressionId,
            eventType
        )
        events.add(event)
        notifyAdEventTracked(event)
    }

    @Synchronized
    private fun performPublishAdEvents() {
        if (session == null || events.isEmpty()) {
            return
        }
        val currentEvents: Set<AdEvent> = HashSet(events)
        events.clear()
        session?.let {
            transporter.dispatchToBackground {
                eventAdapter.sendAdEvents(it, currentEvents)
            }
        }
    }

    private fun performAddListener(listener: Listener) {
        listeners.add(listener)
    }

    private fun performRemoveListener(listener: Listener) {
        listeners.remove(listener)
    }

    @Synchronized
    private fun notifyAdEventTracked(event: AdEvent) {
        for (l in listeners) {
            l.onAdEventTracked(event)
        }
    }

    @Synchronized
    override fun onPublishEvents() {
        transporter.dispatchToBackground {
            performPublishAdEvents()
        }
    }

    override fun onSessionAvailable(session: Session) {
        this.session = session
    }

    override fun onAdsAvailable(session: Session) {
        this.session = session
    }

    fun addListener(listener: Listener) {
        performAddListener(listener)
    }

    fun removeListener(listener: Listener) {
        performRemoveListener(listener)
    }

    fun trackImpression(ad: Ad) {
        transporter.dispatchToBackground {
            fileEvent(ad, AdEventTypes.IMPRESSION)
        }
    }

    fun trackInvisibleImpression(ad: Ad) {
        transporter.dispatchToBackground {
            fileEvent(ad, AdEventTypes.INVISIBLE_IMPRESSION)
        }
    }

    fun trackInteraction(ad: Ad) {
        transporter.dispatchToBackground {
            fileEvent(ad, AdEventTypes.INTERACTION)
        }
    }

    fun trackPopupBegin(ad: Ad) {
        transporter.dispatchToBackground {
            fileEvent(ad, AdEventTypes.POPUP_BEGIN)
        }
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: EventClient

        fun createInstance(eventAdapter: EventAdapter, transporter: TransporterCoroutineScope) {
            this.instance = EventClient(eventAdapter, transporter)
        }

        fun getInstance(): EventClient {
            return instance
        }
    }

    init {
        events = HashSet()
        listeners = HashSet()
        SessionClient.addListener(this)
    }
}
package com.adadapted.library.keyword

import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionClient
import com.adadapted.library.session.SessionListener
import kotlin.jvm.Synchronized
import kotlin.native.concurrent.ThreadLocal

class InterceptClient private constructor(
    private val adapter: InterceptAdapter,
    private val transporter: TransporterCoroutineScope
) : SessionListener {
    interface Listener {
        fun onKeywordInterceptInitialized(intercept: Intercept)
    }

    private val events: MutableSet<InterceptEvent>
    private lateinit var currentSession: Session

    private fun performInitialize(session: Session?, listener: Listener?) {
        if (session == null || listener == null) {
            return
        }
        transporter.dispatchToBackground {
            adapter.retrieve(session, object :
                InterceptAdapter.Listener {
                override fun onSuccess(intercept: Intercept) {
                    listener.onKeywordInterceptInitialized(intercept)
                }
            })
        }
        SessionClient.getInstance().addListener(this)
    }

    @Synchronized
    private fun fileEvent(event: InterceptEvent) {
        val currentEvents: Set<InterceptEvent> = HashSet(events)
        events.clear()
        val resultingEvents = consolidateEvents(event, currentEvents)
        events.addAll(resultingEvents)
    }

    private fun consolidateEvents(
        event: InterceptEvent,
        events: Set<InterceptEvent>
    ): Set<InterceptEvent> {
        val resultingEvents: MutableSet<InterceptEvent> = HashSet(this.events)
        // Create a new Set of Events not superseded by the current Event
        for (e in events) {
            if (!event.supersedes(e)) {
                resultingEvents.add(e)
            }
        }
        resultingEvents.add(event)
        return resultingEvents
    }

    @Synchronized
    private fun performPublishEvents() {
        if (events.isEmpty()) {
            return
        }
        val currentEvents: MutableSet<InterceptEvent> = HashSet(events)
        events.clear()
        transporter.dispatchToBackground {
            adapter.sendEvents(currentSession, currentEvents)
        }
    }

    override fun onSessionAvailable(session: Session) {
        currentSession = session
    }

    override fun onPublishEvents() {
        transporter.dispatchToBackground {
            performPublishEvents()
        }
    }

    fun initialize(session: Session?, listener: Listener?) {
        transporter.dispatchToBackground {
            performInitialize(session, listener)
        }
    }

    @Synchronized
    fun trackMatched(
        searchId: String,
        termId: String,
        term: String,
        userInput: String
    ) {
        trackEvent(searchId, termId, term, userInput, InterceptEvent.MATCHED)
    }

    @Synchronized
    fun trackPresented(
        searchId: String,
        termId: String,
        term: String,
        userInput: String
    ) {
        trackEvent(searchId, termId, term, userInput, InterceptEvent.PRESENTED)
    }

    @Synchronized
    fun trackSelected(
        searchId: String,
        termId: String,
        term: String,
        userInput: String
    ) {
        trackEvent(searchId, termId, term, userInput, InterceptEvent.SELECTED)
    }

    @Synchronized
    fun trackNotMatched(searchId: String, userInput: String) {
        trackEvent(searchId, "", "NA", userInput, InterceptEvent.NOT_MATCHED)
    }

    @Synchronized
    private fun trackEvent(
        searchId: String,
        termId: String,
        term: String,
        userInput: String,
        eventType: String
    ) {

        val event = InterceptEvent(
            searchId,
            eventType,
            userInput,
            termId,
            term
        )

        transporter.dispatchToBackground {
            fileEvent(event)
        }
    }

    @ThreadLocal
    companion object {
        private lateinit var instance: InterceptClient

        fun getInstance(): InterceptClient {
            return instance
        }

        fun createInstance(adapter: InterceptAdapter, transporter: TransporterCoroutineScope) {
            instance = InterceptClient(adapter, transporter)
        }
    }

    init {
        events = HashSet()
        SessionClient.getInstance().addListener(this)
    }
}
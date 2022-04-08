package com.adadapted.library.session

import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.concurrency.Timer
import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.constants.Config
import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.device.DeviceInfoClient
import kotlin.jvm.Synchronized
import kotlin.native.concurrent.ThreadLocal

class SessionClient private constructor(private val adapter: SessionAdapter, private val transporter: TransporterCoroutineScope) : SessionAdapter.Listener {

    enum class Status {
        OK,  // Normal Status. No alterations to regular behavior
        SHOULD_REFRESH,  // SDK should refresh Ads or Reinitialize Session at the next available chance
        IS_REFRESH_ADS,  // SDK is currently refreshing Ads
        IS_REINITIALIZING_SESSION // SDK is currently reinitializing the Session
    }

    private val sessionListeners: MutableSet<SessionListener>
    private val presenters: MutableSet<String>
    private lateinit var currentSession: Session
    var status: Status
        private set
    private var pollingTimerRunning: Boolean
    private var eventTimerRunning: Boolean

    private fun performAddListener(listener: SessionListener) {
        sessionListeners.add(listener)
        if (this::currentSession.isInitialized) {
            listener.onSessionAvailable(currentSession)
        }
    }

    private fun performRemoveListener(listener: SessionListener) {
        sessionListeners.remove(listener)
    }

    private fun performAddPresenter(listener: SessionListener) {
        performAddListener(listener)
        presenters.add(listener.toString())

        if (status == Status.SHOULD_REFRESH) {
            performRefresh()
        }
    }

    private fun performRemovePresenter(listener: SessionListener) {
        performRemoveListener(listener)
        presenters.remove(listener.toString())
    }

    private fun presenterSize() = presenters.size

    private fun performInitialize(deviceInfo: DeviceInfo) {
        transporter.dispatchToBackground { instance?.let { adapter.sendInit(deviceInfo, it) } }
    }

    private fun performRefresh(deviceInfo: DeviceInfo? = DeviceInfoClient.getInstance().getCachedDeviceInfo()) {
        if (currentSession.hasExpired()) {
            print(LOG_TAG + "Session has expired. Expired at: " + currentSession.expiration)
            notifySessionExpired()
            if (deviceInfo != null) {
                performReinitialize(deviceInfo)
            }
        } else {
            performRefreshAds()
        }
    }

    private fun performReinitialize(deviceInfo: DeviceInfo) {
        if (status == Status.OK || status == Status.SHOULD_REFRESH) {
            if (presenterSize() > 0) {
                print(LOG_TAG + "Reinitializing Session.")
                status = Status.IS_REINITIALIZING_SESSION
                transporter.dispatchToBackground { instance?.let {
                    adapter.sendInit(deviceInfo,
                        it
                    )
                } }
            } else {
                status = Status.SHOULD_REFRESH
            }
        }
    }

    private fun performRefreshAds() {
        if (status == Status.OK || status == Status.SHOULD_REFRESH) {
            if (presenterSize() > 0) {
                print(LOG_TAG + "Checking for more Ads.")
                status = Status.IS_REFRESH_ADS
                transporter.dispatchToBackground { instance?.let {
                    adapter.sendRefreshAds(currentSession,
                        it
                    )
                } }
            } else {
                status = Status.SHOULD_REFRESH
            }
        }
    }

    private fun updateCurrentSession(session: Session) {
        currentSession = session
        startPublishTimer()
        startPollingTimer()
    }

    private fun updateCurrentZones(session: Session) {
        currentSession.updateZones(session.getAllZones())
        startPollingTimer()
    }

    private fun startPollingTimer() {
        if (pollingTimerRunning || currentSession.willNotServeAds()) {
            print(LOG_TAG + "Session will not serve Ads. Ignoring Ad polling timer.")
            return
        }
        pollingTimerRunning = true
        print(LOG_TAG + "Starting Ad polling timer.")

        val refreshTimer = Timer({ performRefresh() }, repeatMillis = currentSession.refreshTime, delayMillis = currentSession.refreshTime)
        refreshTimer.startTimer()
    }

    private fun startPublishTimer() {
        if(eventTimerRunning) {
            return
        }
        eventTimerRunning = true
        print(LOG_TAG + "Starting up the Event Publisher.")

        val eventTimer = Timer({ notifyPublishEvents() }, repeatMillis = Config.DEFAULT_EVENT_POLLING, delayMillis = Config.DEFAULT_EVENT_POLLING)
        eventTimer.startTimer()
    }

    private fun notifyPublishEvents() {
        for (l in sessionListeners) {
            l.onPublishEvents()
        }
    }

    private fun notifySessionAvailable() {
        for (l in sessionListeners) {
            l.onSessionAvailable(currentSession)
        }
    }

    private fun notifyAdsAvailable() {
        for (l in sessionListeners) {
            l.onAdsAvailable(currentSession)
        }
    }

    private fun notifySessionInitFailed() {
        for (l in sessionListeners) {
            l.onSessionInitFailed()
        }
    }

    private fun notifySessionExpired() {
        for (l in sessionListeners) {
            l.onSessionExpired()
        }
    }

    override fun onSessionInitialized(session: Session) {
        updateCurrentSession(session)
        notifySessionAvailable()
        status = Status.OK
    }

    override fun onSessionInitializeFailed() {
        updateCurrentSession(Session())
        notifySessionInitFailed()
        status = Status.OK
    }

    override fun onNewAdsLoaded(session: Session) {
        updateCurrentZones(session)
        notifyAdsAvailable()
        status = Status.OK
    }

    override fun onNewAdsLoadFailed() {
        updateCurrentZones(Session())
        notifyAdsAvailable()
        status = Status.OK
    }

    @Synchronized
    fun start(listener: SessionListener) {
        addListener(listener)
        DeviceInfoClient.getInstance().getDeviceInfo(object: DeviceInfoClient.Callback {
            override fun onDeviceInfoCollected(deviceInfo: DeviceInfo) {
                transporter.dispatchToBackground {
                    performInitialize(deviceInfo)
                }
            }
        })
    }

    fun addListener(listener: SessionListener) {
        performAddListener(listener)
    }

    fun removeListener(listener: SessionListener) {
        performRemoveListener(listener)
    }

    fun addPresenter(listener: SessionListener) {
        performAddPresenter(listener)
    }

    fun removePresenter(listener: SessionListener) {
        performRemovePresenter(listener)
    }

    @ThreadLocal
    companion object {
        private var instance: SessionClient? = null

        fun getInstance(): SessionClient? {
            return instance
        }

        fun createInstance(adapter: SessionAdapter, transporter: TransporterCoroutineScope) {
            instance = SessionClient(adapter, transporter)
        }

        fun hasInstance(): Boolean {
            return instance != null
        }
    }

    init {
        sessionListeners = HashSet()
        presenters = HashSet()
        pollingTimerRunning = false
        eventTimerRunning = false
        status = Status.OK
    }
}
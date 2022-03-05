package com.adadapted.library.view

import com.adadapted.library.ad.Ad
import com.adadapted.library.ad.AdActionType
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.session.Session
import com.adadapted.library.session.SessionListener
import com.adadapted.library.PopupAdViewHandler
import com.adadapted.library.ad.AdContentPublisher
import com.adadapted.library.concurrency.Timer
import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.session.SessionClient

internal class AdZonePresenter()
    : SessionListener {
    internal interface Listener {
        fun onZoneAvailable(zone: Zone)
        fun onAdsRefreshed(zone: Zone)
        fun onAdAvailable(ad: Ad)
        fun onNoAdAvailable()
    }

    private var currentAd: Ad = Ad()
    private var zoneId: String = ""
    private var listener: Listener? = null
    private var attached: Boolean
    private var sessionId: String? = null
    private var zoneLoaded: Boolean
    private var currentZone: Zone
    private var randomAdStartPosition: Int
    private var adStarted = false
    private var adCompleted = false
    private var timerRunning = false
    private lateinit var timer: Timer
//    private val adEventClient: AdEventClient = AdEventClient.getInstance()
//    private val appEventClient: AppEventClient = AppEventClient.getInstance()
    private val sessionClient: SessionClient = SessionClient.getInstance()

    fun init(zoneId: String) {
        if (this.zoneId.isEmpty()) {
            this.zoneId = zoneId
            val params: MutableMap<String, String> = HashMap()
            params["zone_id"] = zoneId
            //appEventClient.trackSdkEvent(EventStrings.ZONE_LOADED, params)
        }
    }

    fun onAttach(l: Listener?) {
        if (l == null) {
            println(LOG_TAG + "NULL Listener provided")
            return
        }
        if (!attached) {
            attached = true
            listener = l
            sessionClient.addPresenter(this)
        }
        setNextAd()
    }

    fun onDetach() {
        if (attached) {
            attached = false
            listener = null
            completeCurrentAd()
            sessionClient.removePresenter(this)
        }
    }

    private fun setNextAd() {
        if (!zoneLoaded || timerRunning) {
            return
        }
        completeCurrentAd()
        currentAd = if (listener != null && currentZone.hasAds()) {
            val adPosition = randomAdStartPosition % currentZone.ads.size
            randomAdStartPosition++
            currentZone.ads[adPosition]
        } else {
            Ad()
        }
        adStarted = false
        adCompleted = false
        displayAd()
    }

    private fun displayAd() {
        if (currentAd.isEmpty) {
            notifyNoAdAvailable()
        } else {
            notifyAdAvailable(currentAd)
        }
    }

    private fun completeCurrentAd() {
        if (!currentAd.isEmpty && adStarted && !adCompleted) {
            if (!currentAd.impressionWasTracked()) {
                //adEventClient.trackInvisibleImpression(currentAd)
            }
            currentAd.resetImpressionTracking() //this is critical to make sure rotating ads can get more than one impression (total)
            adCompleted = true
        }
    }

    fun onAdDisplayed(ad: Ad, isAdVisible: Boolean) {
        adStarted = true
        trackAdImpression(ad, isAdVisible)
        startZoneTimer()
    }

    fun onAdVisibilityChanged(isAdVisible: Boolean) {
        trackAdImpression(currentAd, isAdVisible)
    }

    fun onAdDisplayFailed() {
        adStarted = true
        currentAd = Ad()
        startZoneTimer()
    }

    fun onBlankDisplayed() {
        adStarted = true
        currentAd = Ad()
        startZoneTimer()
    }

    private fun trackAdImpression(ad: Ad, isAdVisible: Boolean) {
        if (!isAdVisible || ad.impressionWasTracked() || ad.isEmpty) return
        ad.setImpressionTracked()
        //adEventClient.trackImpression(ad)
    }

    private fun startZoneTimer() {
        if (!zoneLoaded || timerRunning) {
            return
        }
        val timerDelay = currentAd.refreshTime * 1000
        timerRunning = true
        timer = Timer({
            timerRunning = false
            setNextAd()
        }, timerDelay, timerDelay)
    }

    fun onAdClicked(ad: Ad) {
        val actionType = ad.actionType
        val params: MutableMap<String, String> = HashMap()
        params["ad_id"] = ad.id

        when (actionType) {
            AdActionType.CONTENT -> {
                //appEventClient.trackSdkEvent(EventStrings.ATL_AD_CLICKED, params)
                handleContentAction(ad)
            }
            AdActionType.LINK, AdActionType.EXTERNAL_LINK -> {
                //adEventClient.trackInteraction(ad)
                handleLinkAction(ad)
            }
            AdActionType.POPUP -> {
                //adEventClient.trackInteraction(ad)
                handlePopupAction(ad)
            }
            AdActionType.CONTENT_POPUP -> {
                //appEventClient.trackSdkEvent(EventStrings.POPUP_AD_CLICKED, params)
                handlePopupAction(ad)
            }
            else -> println(LOG_TAG + "Cannot handle Action type: $actionType")
        }

        cycleToNextAdIfPossible()
    }

    private fun cycleToNextAdIfPossible() {
        if (currentZone.ads.count() > 1) {
            restartTimer()
            setNextAd()
        }
    }

    private fun restartTimer() {
        timer.cancelTimer()
        timerRunning = false
    }

    private fun handleContentAction(ad: Ad) {
        val zoneId = ad.zoneId
        AdContentPublisher.getInstance().publishContent(zoneId, ad.getContent())
    }

    private fun handleLinkAction(ad: Ad) { //TODO link action extraction
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.data = Uri.parse(ad.actionPath)
//        context.startActivity(intent)
    }

    private fun handlePopupAction(ad: Ad) {
        PopupAdViewHandler.handlePopupAd(ad)
    }

    private fun notifyZoneAvailable() {
        listener?.onZoneAvailable(currentZone)
    }

    private fun notifyAdsRefreshed() {
        listener?.onAdsRefreshed(currentZone)
    }

    private fun notifyAdAvailable(ad: Ad) {
        listener?.onAdAvailable(ad)
    }

    private fun notifyNoAdAvailable() {
        listener?.onNoAdAvailable()
    }

    private fun updateSessionId(sessionId: String): Boolean {
        if (this.sessionId == null || this.sessionId != sessionId) {
            this.sessionId = sessionId
            return true
        }
        return false
    }

    private fun updateCurrentZone(zone: Zone) {
        zoneLoaded = true
        currentZone = zone
        if (currentAd.isEmpty) {
            setNextAd()
        }
    }

    override fun onSessionAvailable(session: Session) {
        updateCurrentZone(zoneId.let { session.getZone(it) })
        if (updateSessionId(session.id)) {
            notifyZoneAvailable()
        }
    }

    override fun onAdsAvailable(session: Session) {
        updateCurrentZone(session.getZone(zoneId))
        notifyAdsRefreshed()
    }

    override fun onSessionInitFailed() {
        updateCurrentZone(Zone())
    }

    init {
        attached = false
        zoneLoaded = false
        currentZone = Zone()
        randomAdStartPosition = ((0..100).random() * 10)
    }
}
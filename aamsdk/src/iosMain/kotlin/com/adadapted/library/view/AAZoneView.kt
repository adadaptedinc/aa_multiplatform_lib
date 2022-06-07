package com.adadapted.library.view

import com.adadapted.library.ad.AdContentListener
import com.adadapted.library.interfaces.ZoneViewListener
import com.adadapted.library.log.AALogger
import platform.UIKit.*

class AAZoneView constructor(zoneId: String, contentListener: AdContentListener? = null): ZoneViewListener {
    var zoneView = IosZoneView()
    var detailedItem = ""

    init {
        zoneView.initZone(zoneId)
        zoneView.setAdContentListener(contentListener)
    }

    fun getZoneView(): IosZoneView {
        zoneView.setZoneViewListener(this)
        zoneView.setNeedsDisplay()
        return zoneView
    }

    fun setVisibility(isVisible: Boolean) {
        zoneView.setAdZoneVisibility(isVisible)
    }

    override fun onZoneHasAds(hasAds: Boolean) {
        AALogger.logInfo("Zone has ads: $hasAds")
    }

    override fun onAdLoaded() {
        AALogger.logInfo("onAdLoaded")
    }

    override fun onAdLoadFailed() {
        AALogger.logInfo("onAdLoadFailed")
    }
}
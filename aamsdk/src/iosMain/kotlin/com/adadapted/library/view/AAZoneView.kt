package com.adadapted.library.view

import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.interfaces.ZoneViewListener
import platform.UIKit.UIView
import platform.UIKit.setNeedsDisplay

class AAZoneView constructor(zoneId: String): ZoneViewListener {
    var zoneView = IosZoneView()

    init {
        zoneView.onStart(this)
        zoneView.initZone(zoneId)
    }

    fun getZoneView(): UIView {
        zoneView.setNeedsDisplay()
        return zoneView
    }

    fun setVisibility(isVisible: Boolean) {
        zoneView.setAdZoneVisibility(isVisible)
    }

    override fun onZoneHasAds(hasAds: Boolean) {
        println("Zone has ads: $hasAds")
    }

    override fun onAdLoaded() {
        println("onAdLoaded")
    }

    override fun onAdLoadFailed() {
        println("onAdLoadFailed")
    }
}
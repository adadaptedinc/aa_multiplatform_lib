package com.adadapted.library.view

import com.adadapted.library.interfaces.ZoneViewListener
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.*

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
package com.adadapted.library.interfaces

interface ZoneViewListener {
    fun onZoneHasAds(hasAds: Boolean)
    fun onAdLoaded()
    fun onAdLoadFailed()
}
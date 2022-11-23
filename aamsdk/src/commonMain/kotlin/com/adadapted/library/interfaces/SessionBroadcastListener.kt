package com.adadapted.library.interfaces

interface SessionBroadcastListener {
    fun onHasAdsToServe(hasAds: Boolean, availableZoneIds: List<String>)
}
package com.adadapted.library.interfaces

interface EventBroadcastListener {
    fun onAdEventTracked(zoneId: String, eventType: String)
}
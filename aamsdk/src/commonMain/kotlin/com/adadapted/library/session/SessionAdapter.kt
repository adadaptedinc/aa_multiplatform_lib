package com.adadapted.library.session

import com.adadapted.library.device.DeviceInfo

interface SessionAdapter {
    interface SessionInitListener {
        fun onSessionInitialized(session: Session)
        fun onSessionInitializeFailed()
    }

    interface AdGetListener {
        fun onNewAdsLoaded(session: Session)
        fun onNewAdsLoadFailed()
    }

    interface Listener : SessionInitListener, AdGetListener

    suspend fun sendInit(deviceInfo: DeviceInfo, listener: SessionInitListener)
    suspend fun sendRefreshAds(session: Session, listener: AdGetListener)
}
package com.adadapted.library.interfaces

import com.adadapted.library.device.DeviceInfo
import com.adadapted.library.session.Session

interface SessionAdapter {
    suspend fun sendInit(deviceInfo: DeviceInfo, listener: SessionInitListener)
    suspend fun sendRefreshAds(session: Session, listener: AdGetListener)
}
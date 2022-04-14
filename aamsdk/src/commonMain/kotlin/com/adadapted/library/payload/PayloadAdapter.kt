package com.adadapted.library.payload

import com.adadapted.library.atl.AddItContent
import com.adadapted.library.device.DeviceInfo

interface PayloadAdapter {
    suspend fun pickup(deviceInfo: DeviceInfo, callback: (content: List<AddItContent>) -> Unit)
    suspend fun publishEvent(deviceInfo: DeviceInfo, event: PayloadEvent)
}
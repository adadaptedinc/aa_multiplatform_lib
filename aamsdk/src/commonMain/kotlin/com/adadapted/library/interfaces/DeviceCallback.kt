package com.adadapted.library.interfaces

import com.adadapted.library.device.DeviceInfo

interface DeviceCallback {
    fun onDeviceInfoCollected(deviceInfo: DeviceInfo)
}
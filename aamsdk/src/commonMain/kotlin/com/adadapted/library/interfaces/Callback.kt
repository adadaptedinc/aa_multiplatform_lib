package com.adadapted.library.interfaces

import com.adadapted.library.device.DeviceInfo

interface Callback {
    fun onDeviceInfoCollected(deviceInfo: DeviceInfo)
}
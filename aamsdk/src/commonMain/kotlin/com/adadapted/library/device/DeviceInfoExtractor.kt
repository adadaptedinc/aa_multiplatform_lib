package com.adadapted.library.device

expect class DeviceInfoExtractor {
    fun extractDeviceInfo(appId: String, isProd: Boolean, customIdentifier: String, params: Map<String, String>): DeviceInfo
}
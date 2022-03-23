package com.adadapted.library.device

expect class DeviceInfoExtractor {
    fun extractDeviceInfo(appId: String, isProd: Boolean, params: Map<String, String>, customIdentifier: String): DeviceInfo
}
package com.adadapted.library.device

import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.concurrency.TransporterCoroutineScope
import com.adadapted.library.interfaces.DeviceCallback
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object DeviceInfoClient {

    private var appId: String = ""
    private var isProd: Boolean = false
    private var params: Map<String, String> = emptyMap()
    private var customIdentifier: String =""
    private var deviceInfoExtractor: DeviceInfoExtractor? = null
    private var transporter: TransporterCoroutineScope = Transporter()
    private var deviceInfo: DeviceInfo? = null
    private var deviceCallbacks: MutableSet<DeviceCallback> = HashSet()

    private fun performGetInfo(deviceCallback: DeviceCallback) {
        if (deviceInfo != null) {
            deviceInfo?.let { deviceCallback.onDeviceInfoCollected(it) }
        } else {
            deviceCallbacks.add(deviceCallback)
        }
    }

    private fun collectDeviceInfo() {
        this.deviceInfo = deviceInfoExtractor?.extractDeviceInfo(appId, isProd, customIdentifier, params)
        notifyCallbacks()
    }

    private fun notifyCallbacks() {
        val currentDeviceCallbacks: Set<DeviceCallback> = HashSet(deviceCallbacks)
        for (caller in currentDeviceCallbacks) {
            deviceInfo?.let { caller.onDeviceInfoCollected(it) }
            deviceCallbacks.remove(caller)
        }
    }

    fun getDeviceInfo(deviceCallback: DeviceCallback) {
        transporter.dispatchToThread {
            performGetInfo(deviceCallback)
        }
    }

    fun getCachedDeviceInfo(): DeviceInfo? {
        return if (deviceInfo != null) {
            deviceInfo
        } else {
            null
        }
    }

    fun createInstance(
        appId: String,
        isProd: Boolean,
        params: Map<String, String>,
        customIdentifier: String,
        deviceInfoExtractor: DeviceInfoExtractor,
        transporter: TransporterCoroutineScope
    ) {
            this.appId = appId
            this.isProd = isProd
            this.params = params
            this.customIdentifier = customIdentifier
            this.deviceInfoExtractor = deviceInfoExtractor
            this.transporter = transporter

        transporter.dispatchToThread {
            collectDeviceInfo()
        }
    }
}
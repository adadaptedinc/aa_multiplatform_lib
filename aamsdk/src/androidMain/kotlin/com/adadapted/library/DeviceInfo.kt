package com.adadapted.library

import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
actual data class DeviceInfo(
    @SerialName("app_id")val appId: String = "",
    val isProd: Boolean = false,
    val scale: Float = 0f,
    val bundleId: String? = null,
    val bundleVersion: String? = null,
    @SerialName("udid") val udid: String = "",
    @SerialName("device_name") val device: String = "",
    @SerialName("device_udid") val deviceUdid: String = "",
    @SerialName("device_os") val os: String = OS,
    @SerialName("device_osv") val osv: String = "",
    @SerialName("device_local") val locale: String = "",
    @SerialName("device_timezone") val timezone: String = "",
    @SerialName("device_carrier") val carrier: String = "",
    @SerialName("device_width") val dw: Int = 0,
    @SerialName("device_height") val dh: Int = 0,
    @SerialName("device_density") val density: Int = 0,
    @SerialName("allow_retargeting") val isAllowRetargetingEnabled: Boolean = false,
    @SerialName("sdk_version") val sdkVersion: String = "X.X.X",//BuildConfig.VERSION_NAME TODO CHANGE
    @SerialName("created_at") val createdAt: Long = Clock.System.now().epochSeconds,
    val params: Map<String, String> = mapOf()
) {

    companion object {
        const val UNKNOWN_VALUE = "Unknown"
        const val OS = "Android"
        fun empty(): DeviceInfo {
            return DeviceInfo()
        }
    }
}
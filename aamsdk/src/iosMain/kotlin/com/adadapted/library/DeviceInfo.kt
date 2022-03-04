package com.adadapted.library

import platform.AppTrackingTransparency.ATTrackingManager
import platform.Foundation.*
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
actual data class DeviceInfo(
    @SerialName("app_id")val appId: String = "",
    val isProd: Boolean = false,
    val bundleId: String? = null,
    val bundleVersion: String? = null,
    @SerialName("udid") val udid: String? = UIDevice.currentDevice.identifierForVendor?.UUIDString,
    @SerialName("device_name") val device: String = UIDevice.currentDevice.name,
    @SerialName("device_udid") val deviceUdid: String = "",
    @SerialName("device_os") val os: String = OS,
    @SerialName("device_osv") val osv: String = UIDevice.currentDevice.systemVersion,
    @SerialName("device_local") val locale: String? = NSLocale.currentLocale.countryCode,
    @SerialName("device_timezone") val timezone: String = NSTimeZone.defaultTimeZone.name,
    @SerialName("device_carrier") val carrier: String = platform.CoreTelephony.CTTelephonyNetworkInfo().serviceSubscriberCellularProviders()?.get("serviceSubscriberCellularProvider").toString(),
    @SerialName("device_width") val dw: Int = 0,
    @SerialName("device_height") val dh: Int = 0,
    @SerialName("device_density") val density: Int = 0,
    @SerialName("allow_retargeting") val isAllowRetargetingEnabled: Boolean = ATTrackingManager.trackingAuthorizationStatus.toInt() == 3,
    @SerialName("sdk_version") val sdkVersion: String = "X.X.X",//BuildConfig.VERSION_NAME TODO CHANGE
    @SerialName("created_at") val createdAt: Long = Clock.System.now().epochSeconds,
    val params: Map<String, String> = mapOf()
){
   companion object {
           const val UNKNOWN_VALUE = "Unknown"
           const val OS = "iPhone OS"
           fun empty(): DeviceInfo {
               return DeviceInfo()
           }
   }
}

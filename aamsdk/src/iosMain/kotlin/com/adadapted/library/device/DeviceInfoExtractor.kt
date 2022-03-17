package com.adadapted.library.device

import platform.AppTrackingTransparency.ATTrackingManager
import platform.Foundation.*
import platform.UIKit.UIDevice

actual class DeviceInfoExtractor {
    //TODO build and return a device info object and remove all these methods
    actual fun extractDeviceInfo(
        appId: String,
        isProd: Boolean,
        customIdentifier: String,
        params: Map<String, String>
    ): DeviceInfo {
        TODO("Not yet implemented")
    }

//    fun getUDID(): String {
//        return UIDevice.currentDevice.identifierForVendor?.UUIDString ?: "00000" //todo actual zero string
//    }
//
//    fun getDeviceName(): String {
//        return UIDevice.currentDevice.name
//    }
//
//    fun getOS(): String {
//        return "iPhone OS"
//    }
//
//    fun getOSV(): String {
//        return UIDevice.currentDevice.systemVersion
//    }
//
//    fun getLocale(): String {
//        return NSLocale.currentLocale.countryCode ?: ""
//    }
//
//    fun getTimeZone(): String {
//        return NSTimeZone.defaultTimeZone.name
//    }
//
//    fun getDeviceCarrier(): String {
//        return platform.CoreTelephony.CTTelephonyNetworkInfo().serviceSubscriberCellularProviders()?.get("serviceSubscriberCellularProvider").toString()
//    }
//
//    fun getRetargetingEnabled(): Boolean {
//        return ATTrackingManager.trackingAuthorizationStatus.toInt() == 3
//    }
}
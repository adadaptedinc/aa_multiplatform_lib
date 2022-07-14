package com.adadapted.library.device

import com.adadapted.library.constants.Config
import kotlinx.datetime.Clock
import platform.AdSupport.ASIdentifierManager
import platform.AppTrackingTransparency.ATTrackingManager
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetWidth
import platform.Foundation.*
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import platform.darwin.nil

actual class DeviceInfoExtractor {
    private val isAllowRetargetingEnabled = ATTrackingManager.trackingAuthorizationStatus.toInt() == 3
    private val screenSize = UIScreen.mainScreen().bounds()
    private val width = CGRectGetWidth(screenSize)
    private val height = CGRectGetHeight(screenSize)

    private fun generateUdid(): String {
        return NSUUID().UUIDString.replace("_", "")
    }

    private fun getUdid(): String {
        val AA_UUID_KEY = "aamsdk_uuid"
        val preferences = NSUserDefaults.standardUserDefaults()
        val id: String

        if (isAllowRetargetingEnabled) {
            id = ASIdentifierManager.sharedManager().advertisingIdentifier.UUIDString
            preferences.setValue(id, forKey = AA_UUID_KEY)
        } else if (preferences.objectForKey(AA_UUID_KEY) == nil) {
            id = generateUdid()
            preferences.setValue(id, forKey = AA_UUID_KEY)
        } else {
            id = preferences.valueForKey(AA_UUID_KEY).toString()
        }
        return id
    }

    actual fun extractDeviceInfo(appId: String, isProd: Boolean, customIdentifier: String, params: Map<String, String>): DeviceInfo {
        return DeviceInfo(
            appId = appId,
            isProd = isProd,
            customIdentifier = customIdentifier,
            params = params,
            bundleId = NSBundle.mainBundle().bundleIdentifier().toString(),
            deviceName = UIDevice.currentDevice.name,
            deviceUdid = getUdid(),
            os = "iOS",
            osv = UIDevice.currentDevice.systemVersion,
            timezone = NSTimeZone.defaultTimeZone.name,
            locale = NSLocale.currentLocale.countryCode.toString(),
            udid = getUdid(),
            isAllowRetargetingEnabled = isAllowRetargetingEnabled,
            bundleVersion = NSBundle.mainBundle().objectForInfoDictionaryKey("CFBundleShortVersionString").toString(),
            carrier = platform.CoreTelephony.CTTelephonyNetworkInfo()
                .serviceSubscriberCellularProviders()?.get("serviceSubscriberCellularProvider").toString(),
            scale = UIScreen.mainScreen.scale.toFloat(),
            dh = height.toInt(),
            dw = width.toInt(),
            density = UIScreen.mainScreen.scale.toString(),
            sdkVersion = Config.LIBRARY_VERSION,
            createdAt = Clock.System.now().epochSeconds
        )
    }
}

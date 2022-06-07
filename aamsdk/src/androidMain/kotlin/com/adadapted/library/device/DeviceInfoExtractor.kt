package com.adadapted.library.device

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.adadapted.library.constants.Config
import com.adadapted.library.constants.Config.LOG_TAG
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.io.IOException
import java.util.TimeZone
import java.util.Locale

actual class DeviceInfoExtractor(context: Context) {
    private var contextRef: Context? = context

    actual fun extractDeviceInfo(appId: String, isProd: Boolean, customIdentifier: String, params: Map<String, String>,): DeviceInfo {
        var mUdid: String
        var mAllowRetargeting = false
        var mScale = 0f
        var mHeight = 0
        var mWidth = 0
        var mDensity = 0

        if (customIdentifier.isNotEmpty()) {
            mUdid = customIdentifier
        } else {
            try {
                Class.forName(AdvertisingIdClientName)
                val info = contextRef?.let { getAdvertisingIdClientInfo(it) }
                if (info != null) {
                    mUdid = info.id
                    mAllowRetargeting = (!info.isLimitAdTrackingEnabled)
                } else {
                    mUdid = captureAndroidId(contextRef)
                    mAllowRetargeting = false
                }
            } catch (e: ClassNotFoundException) {
                mUdid = captureAndroidId(contextRef)
                mAllowRetargeting = false
            }
        }

        val mBundleVersion: String = try {
            val packageInfo = contextRef?.packageName?.let { contextRef?.packageManager?.getPackageInfo(it, 0) }
            val version =
                if (packageInfo != null) packageInfo.versionName else DeviceInfo.UNKNOWN_VALUE
            version
        } catch (ex: PackageManager.NameNotFoundException) {
            DeviceInfo.UNKNOWN_VALUE
        }

        val manager = contextRef?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val mCarrier = manager.networkOperatorName.ifEmpty { NetworkOperatorDefault }

        val metrics = contextRef?.resources?.displayMetrics
        if (metrics != null) {
            mScale = metrics.density
            mHeight = metrics.heightPixels
            mWidth = metrics.widthPixels
            mDensity = metrics.densityDpi
        }

        val mBundleId = contextRef?.packageName ?: ""
        val mDeviceUdid = captureAndroidId(contextRef)

        wipeContextReference()

        return DeviceInfo(
            appId = appId,
            isProd = isProd,
            params = params,
            bundleId = mBundleId,
            deviceName = Build.MANUFACTURER + " " + Build.MODEL,
            deviceUdid = mDeviceUdid,
            os = "Android",
            osv = Build.VERSION.SDK_INT.toString(),
            timezone = TimeZone.getDefault().id,
            locale = Locale.getDefault().toString(),
            udid = mUdid,
            isAllowRetargetingEnabled = mAllowRetargeting,
            bundleVersion = mBundleVersion,
            carrier = mCarrier,
            scale = mScale,
            dh = mHeight,
            dw = mWidth,
            density = mDensity.toString(),
            sdkVersion = Config.LIBRARY_VERSION
        )
    }

    private fun getAdvertisingIdClientInfo(context: Context): AdvertisingIdClient.Info? {
        if (isTrackingDisabled(context)) {
            return null
        }

        try {
            return AdvertisingIdClient.getAdvertisingIdInfo(context.applicationContext)
        } catch (ex: GooglePlayServicesNotAvailableException) {
            trackGooglePlayAdError(ex)
        } catch (ex: GooglePlayServicesRepairableException) {
            trackGooglePlayAdError(ex)
        } catch (ex: IOException) {
            trackGooglePlayAdError(ex)
        }
        return null
    }

    private fun captureAndroidId(context: Context?): String {
        @SuppressLint("HardwareIds") val androidId =
            Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
        return androidId ?: getOrGenerateCustomId(context)
    }

    private fun getOrGenerateCustomId(context: Context?): String {
        if (context == null) {
            return DeviceIdGenerator.generateId()
        }

        val sharedPrefs = context.getSharedPreferences(Config.AASDK_PREFS_KEY, Context.MODE_PRIVATE)
        val generatedId = sharedPrefs.getString(Config.AASDK_PREFS_GENERATED_ID_KEY, "")

        if (generatedId.isNullOrEmpty()) {
            val newGeneratedId = DeviceIdGenerator.generateId()
            with(sharedPrefs.edit()) {
                putString(Config.AASDK_PREFS_GENERATED_ID_KEY, newGeneratedId)
                apply()
            }
            return newGeneratedId
        }
        return generatedId
    }

    private fun isTrackingDisabled(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(Config.AASDK_PREFS_KEY, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(Config.AASDK_PREFS_TRACKING_DISABLED_KEY, false)
    }

    private fun wipeContextReference() {
        //IMPORTANT to clear out the context reference after using it
        this.contextRef = null
    }

    private fun trackGooglePlayAdError(ex: Exception) {
        Log.w(LOG_TAG, GooglePlayAdError)
    }

    companion object {
        private const val GooglePlayAdError = "Problem retrieving Google Play Advertiser Info"
        private const val AdvertisingIdClientName = "com.google.android.gms.ads.identifier.AdvertisingIdClient"
        private const val NetworkOperatorDefault = "None"
    }
}
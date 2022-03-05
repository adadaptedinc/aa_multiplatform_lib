package com.adadapted.library.session

import com.adadapted.library.DeviceInfo
import com.adadapted.library.view.Zone
import com.adadapted.library.constants.Config
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Session(
    @SerialName("session_id")
    val id: String = "",

    @SerialName("will_serve_ads")
    private val willServeAds: Boolean = false,

    @SerialName("active_campaigns")
    val hasAds: Boolean = false,

    @SerialName("polling_interval_ms")
    val refreshTime: Long = Config.DEFAULT_AD_POLLING,

    @SerialName("session_expires_at")
    val expiration: Long = 0,

    val zones: Map<String, Zone> = HashMap()
) {

    var deviceInfo: DeviceInfo = DeviceInfo()

    fun hasActiveCampaigns(): Boolean {
        return hasAds
    }

    fun hasExpired(): Boolean {
        return convertExpirationToDate(expiration) > (Clock.System.now().epochSeconds)
    }

    fun getZone(zoneId: String): Zone {
        if (zones.containsKey(zoneId)) {
            return zones[zoneId] ?: Zone()
        }
        return Zone()
    }

    fun hasZoneAds(): Boolean {
        for (zone in zones) {
            return zone.value.ads.any()
        }
        return false
    }

    fun willNotServeAds(): Boolean {
        return !willServeAds || refreshTime == 0L
    }

    companion object {
        fun convertExpirationToDate(expireTime: Long): Long {
            return (expireTime * 1000)
        }
    }
}
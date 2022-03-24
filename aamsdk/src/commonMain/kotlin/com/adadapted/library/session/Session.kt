package com.adadapted.library.session

import com.adadapted.library.device.DeviceInfo
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

    private var zones: Map<String, Zone> = HashMap()
) {

    var deviceInfo: DeviceInfo = DeviceInfo()

    fun hasActiveCampaigns(): Boolean {
        return hasAds
    }

    fun hasExpired(): Boolean {
        return Clock.System.now().epochSeconds > expiration
    }

    fun getZone(zoneId: String): Zone {
        if (zones.containsKey(zoneId)) {
            return zones[zoneId] ?: Zone()
        }
        return Zone()
    }

    fun getAllZones(): Map<String, Zone> {
        return zones
    }

    fun hasZoneAds(): Boolean {
        for (zone in zones) {
            return zone.value.ads.any()
        }
        return false
    }

    fun updateZones(newZones: Map<String, Zone>) {
        zones = newZones
    }

    fun willNotServeAds(): Boolean {
        return !willServeAds || refreshTime == 0L
    }
}
package com.adadapted.library.view

import com.adadapted.library.ad.Ad
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Zone(val id: String = "", val ads: List<Ad> = listOf()) {

    @SerialName("port_height")
    val portHeight: Long = 0

    @SerialName("port_width")
    val portWidth: Long = 0

    @SerialName("land_height")
    val landHeight: Long = 0

    @SerialName("land_width")
    val landWidth: Long = 0

    fun hasAds(): Boolean {
        return ads.isNotEmpty()
    }
}
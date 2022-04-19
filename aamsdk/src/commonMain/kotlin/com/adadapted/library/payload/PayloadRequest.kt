package com.adadapted.library.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class PayloadRequest(
    @SerialName("app_id")
    val appId: String,
    val udid: String,
    @SerialName("bundle_id")
    val bundleId: String,
    @SerialName("bundle_version")
    val bundle_version: String,
    val device: String,
    val os: String,
    val osv: String,
    @SerialName("sdk_version")
    val sdkVersion: String,
    val timestamp: Long
)
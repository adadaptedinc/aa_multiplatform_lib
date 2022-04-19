package com.adadapted.library.payload

import kotlinx.serialization.Serializable

@Serializable
data class PayloadResponse(val payloads: List<Payload>)
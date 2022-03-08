package com.adadapted.library.payload

import com.adadapted.library.atl.AddToListItem
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Payload (
    @SerialName("detailed_list_items")
    val detailedListItems: List<AddToListItem> = listOf()
)
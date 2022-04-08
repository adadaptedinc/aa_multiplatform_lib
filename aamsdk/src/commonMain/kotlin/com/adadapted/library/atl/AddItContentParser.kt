package com.adadapted.library.atl

import com.adadapted.library.atl.AddItContent.AddItSources.PAYLOAD
import com.adadapted.library.atl.AddToListContent.Sources.OUT_OF_APP
import com.adadapted.library.constants.AddToListTypes
import com.adadapted.library.payload.PayloadResponse

object AddItContentParser {
    fun generateAddItContentFromPayloads(payloadResponse: PayloadResponse): List<AddItContent> {
        val listOfAddItContentToReturn = payloadResponse.payloads.map {
            AddItContent(
                it.payloadId,
                it.payloadMessage,
                it.payloadImage,
                AddToListTypes.ADD_TO_LIST_ITEMS,
                OUT_OF_APP,
                PAYLOAD,
                it.detailedListItems
            )
        }
        //track errors
        return listOfAddItContentToReturn
    }
}
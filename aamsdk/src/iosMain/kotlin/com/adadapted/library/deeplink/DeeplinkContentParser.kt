package com.adadapted.library.deeplink

import com.adadapted.library.atl.AddItContentParser
import com.adadapted.library.atl.AddItContentPublisher
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.constants.EventStrings.NO_DEEPLINK_URL
import com.adadapted.library.event.EventClient
import com.adadapted.library.helpers.Base64.base64decoded
import com.adadapted.library.payload.Payload
import kotlinx.serialization.decodeFromString
import platform.Foundation.*

class DeeplinkContentParser {

    @Throws(Exception::class)
    fun handleDeeplink(userActivity: NSUserActivity?) {
        val url = userActivity?.webpageURL?.absoluteString as String
        val queryItems = NSURLComponents.componentsWithString(url)?.queryItems

        if (queryItems != null) {
            val queryItem = queryItems.first() as NSURLQueryItem
            if (queryItem.name == "data") {
                val data = queryItem.value as String
                val decodedData = data.base64decoded

                val addItContent = kotlinx.serialization.json.Json.decodeFromString<Payload>(decodedData)
                val content = AddItContentParser.generateAddItContentFromDeeplink(addItContent)
                AddItContentPublisher.publishAddItContent(content)
            }
        } else {
            EventClient.trackSdkError(
                EventStrings.ADDIT_DEEPLINK_HANDLING_ERROR,
                "Problem handling deeplink."
            )
            throw Exception(NO_DEEPLINK_URL)
        }
    }
}

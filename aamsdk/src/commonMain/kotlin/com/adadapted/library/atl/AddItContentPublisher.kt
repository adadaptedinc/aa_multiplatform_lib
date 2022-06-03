package com.adadapted.library.atl

import com.adadapted.library.ad.AdContent
import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.event.EventClient
import kotlin.native.concurrent.ThreadLocal

class AddItContentPublisher private constructor(private val transporter: Transporter) {
    private val publishedContent: MutableMap<String, AddItContent> = HashMap()
    private lateinit var listener: (atlContent: AddToListContent) -> Unit

    fun addListener(listener: (atlContent: AddToListContent) -> Unit) {
        this.listener = listener
    }

    fun publishAddItContent(content: AddItContent) {
        if (content.hasNoItems()) {
            return
        }
        if (!::listener.isInitialized) {
            EventClient.trackSdkError(EventStrings.NO_ADDIT_CONTENT_LISTENER, LISTENER_REGISTRATION_ERROR)
            return
        }
        if (publishedContent.containsKey(content.payloadId)) {
            content.duplicate()
        } else if (::listener.isInitialized) {
            publishedContent[content.payloadId] = content
            notifyContentAvailable(content)
        }
    }

    fun publishPopupContent(content: PopupContent) {
        if (content.hasNoItems()) {
            return
        }
        if (!::listener.isInitialized) {
            EventClient.trackSdkError(EventStrings.NO_ADDIT_CONTENT_LISTENER, LISTENER_REGISTRATION_ERROR)
            return
        }
        notifyContentAvailable(content)
    }

    fun publishAdContent(content: AdContent) {
        if (content.hasNoItems()) {
            return
        }
        if (!::listener.isInitialized) {
            EventClient.trackSdkError(EventStrings.NO_ADDIT_CONTENT_LISTENER, LISTENER_REGISTRATION_ERROR)
            return
        }
        notifyContentAvailable(content)
    }

    private fun notifyContentAvailable(content: AddToListContent) {
        transporter.dispatchToBackground {
            listener.invoke(content)
        }
    }

    @ThreadLocal
    companion object {
        private const val LISTENER_REGISTRATION_ERROR = "App did not register an AddIt Content listener"
        private lateinit var instance: AddItContentPublisher

        fun getInstance(transporter: Transporter = Transporter()): AddItContentPublisher {
            if (!this::instance.isInitialized) {
                instance = AddItContentPublisher(transporter)
            }
            return instance
        }
    }
}

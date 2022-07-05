package com.adadapted.library.atl

import com.adadapted.library.ad.AdContent
import com.adadapted.library.concurrency.Transporter
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.constants.EventStrings.LISTENER_REGISTRATION_ERROR
import com.adadapted.library.event.EventClient
import com.adadapted.library.interfaces.AddItContentListener
import com.adadapted.library.log.AALogger
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object AddItContentPublisher {

    private var transporter: Transporter = Transporter()
    private val publishedContent: MutableMap<String, AddItContent> = HashMap()
    private var listener: AddItContentListener? = null

    fun addListener(listener: AddItContentListener) {
        this.listener = listener
    }

    fun publishAddItContent(content: AddItContent) {
        if (content.hasNoItems()) {
            return
        }
        if (listener == null) {
            EventClient.trackSdkError(EventStrings.NO_ADDIT_CONTENT_LISTENER, LISTENER_REGISTRATION_ERROR)
            contentListenerNotAdded()
            return
        }
        if (publishedContent.containsKey(content.payloadId)) {
            content.duplicate()
        } else if (listener != null) {
            publishedContent[content.payloadId] = content
            notifyContentAvailable(content)
        }
    }

    fun publishPopupContent(content: PopupContent) {
        if (content.hasNoItems()) {
            return
        }
        if (listener == null) {
            EventClient.trackSdkError(EventStrings.NO_ADDIT_CONTENT_LISTENER, LISTENER_REGISTRATION_ERROR)
            contentListenerNotAdded()
            return
        }
        notifyContentAvailable(content)
    }

    fun publishAdContent(content: AdContent) {
        if (content.hasNoItems()) {
            return
        }
        if (listener == null) {
            EventClient.trackSdkError(EventStrings.NO_ADDIT_CONTENT_LISTENER, LISTENER_REGISTRATION_ERROR)
            contentListenerNotAdded()
            return
        }
        notifyContentAvailable(content)
    }

    private fun notifyContentAvailable(content: AddToListContent) {
        transporter.dispatchToMain {
            listener?.onContentAvailable(content)
        }
    }

    private fun contentListenerNotAdded() {
        AALogger.logError(LISTENER_REGISTRATION_ERROR)
    }
}

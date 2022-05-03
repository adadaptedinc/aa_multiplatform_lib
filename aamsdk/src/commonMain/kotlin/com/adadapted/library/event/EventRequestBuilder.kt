package com.adadapted.library.event

import com.adadapted.library.session.Session

object EventRequestBuilder {
    fun buildAdEventRequest(session: Session, adEvents: Set<AdEvent>): AdEventRequest {
        return AdEventRequest(
            session.id,
            session.deviceInfo.appId,
            session.deviceInfo.udid,
            session.deviceInfo.sdkVersion,
            adEvents.toList()
        )
    }
}
package com.adadapted.library.keyword

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class InterceptEvent(
    val searchId: String = "",
    val event: String = "",
    val userInput: String = "",
    val termId: String = "",
    val term: String = ""
) {
    val createdAt: Long = Clock.System.now().epochSeconds

    fun supersedes(e: InterceptEvent): Boolean {
        return event == e.event && termId == e.termId && userInput.contains(e.userInput)
    }

    companion object {
        const val MATCHED = "matched"
        const val NOT_MATCHED = "not_matched"
        const val PRESENTED = "presented"
        const val SELECTED = "selected"
    }
}
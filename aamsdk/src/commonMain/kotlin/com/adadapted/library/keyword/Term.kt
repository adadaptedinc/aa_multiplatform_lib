package com.adadapted.library.keyword

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Term(
    @SerialName("term_id")
    val termId: String,
    val term: String,
    val replacement: String,
    val icon: String,
    val tagLine: String,
    private val priority: Int
) {
    operator fun compareTo(a2: Term): Int {
        if (priority == a2.priority) {
            return term.compareTo(a2.term)
        } else if (priority < a2.priority) {
            return -1
        }
        return 1
    }
}
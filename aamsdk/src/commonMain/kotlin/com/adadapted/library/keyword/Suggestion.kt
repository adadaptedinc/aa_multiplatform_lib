package com.adadapted.library.keyword

import com.adadapted.library.keyword.SuggestionTracker.suggestionPresented
import com.adadapted.library.keyword.SuggestionTracker.suggestionSelected

class Suggestion(val searchId: String, term: Term) {
    val termId: String = term.termId
    val name: String = term.replacement
    val icon: String = term.icon
    val tagLine: String = term.tagLine
    var presented: Boolean
    var selected: Boolean

    init {
        presented = false
        selected = false
    }

    fun presented() {
        if (!presented) {
            presented = true
            suggestionPresented(searchId, termId, name)
        }
    }

    fun selected() {
        if (!selected) {
            selected = true
            suggestionSelected(searchId, termId, name)
        }
    }
}
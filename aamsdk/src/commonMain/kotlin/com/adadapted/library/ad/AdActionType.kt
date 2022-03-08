package com.adadapted.library.ad

object AdActionType {
    const val CONTENT = "c"
    const val CONTENT_POPUP = "cp"
    const val POPUP = "p"
    const val LINK = "l"
    const val EXTERNAL_LINK = "e"

    fun handlesContent(type: String): Boolean {
        return CONTENT == type || CONTENT_POPUP == type
    }
}
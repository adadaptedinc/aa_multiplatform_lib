package com.adadapted.library

import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.event.EventClient
import kotlin.jvm.Synchronized

object AdAdaptedListManager {
    private const val LIST_NAME = "list_name"
    private const val ITEM_NAME = "item_name"

    @Synchronized
    fun itemAddedToList(item: String, list: String = "") {
        if (item.isEmpty()) {
            return
        }
        EventClient.trackSdkEvent(EventStrings.USER_ADDED_TO_LIST, generateListParams(list, item))
        println("$LOG_TAG$item was added to $list")
    }

    @Synchronized
    fun itemCrossedOffList(item: String, list: String = "") {
        if (item.isEmpty()) {
            return
        }
        EventClient.trackSdkEvent(EventStrings.USER_CROSSED_OFF_LIST, generateListParams(list, item))
        println("$LOG_TAG$item was crossed off $list")
    }

    @Synchronized
    fun itemDeletedFromList(item: String, list: String = "") {
        if (item.isEmpty()) {
            return
        }
        EventClient.trackSdkEvent(EventStrings.USER_DELETED_FROM_LIST, generateListParams(list, item))
        println("$LOG_TAG$item was deleted from $list")
    }

    private fun generateListParams(list: String, item: String): MutableMap<String, String> {
        val params = HashMap<String, String>()
        params[LIST_NAME] = list
        params[ITEM_NAME] = item
        return params
    }
}
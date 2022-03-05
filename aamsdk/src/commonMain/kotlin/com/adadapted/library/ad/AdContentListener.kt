package com.adadapted.library.ad

import com.adadapted.library.atl.AddToListContent

interface AdContentListener {
    fun onContentAvailable(zoneId: String, content: AddToListContent)
}
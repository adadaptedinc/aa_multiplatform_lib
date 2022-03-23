package com.adadapted.library.view

import com.adadapted.library.ad.Ad

expect class AdViewHandler {
    fun handleLink(ad: Ad)
    fun handlePopup(ad: Ad)
}
package com.adadapted.library.view

import com.adadapted.library.ad.Ad

actual class AdViewHandler {
    actual fun handleLink(ad: Ad) {
        Popup(ad = ad, linkType = LinkType.EXTERNAL)
    }

    actual fun handlePopup(ad: Ad) {
        Popup(ad = ad, linkType = LinkType.POP_UP)
    }
}
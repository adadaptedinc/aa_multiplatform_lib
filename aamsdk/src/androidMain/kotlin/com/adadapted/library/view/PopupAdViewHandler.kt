package com.adadapted.library.view

import com.adadapted.library.ad.Ad

actual object PopupAdViewHandler {
    actual fun handlePopupAd(ad: Ad) {
        AndroidWebViewPopupActivity().createActivity(ad)
    }
}
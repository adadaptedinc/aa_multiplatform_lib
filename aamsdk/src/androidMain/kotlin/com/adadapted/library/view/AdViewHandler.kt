package com.adadapted.library.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.adadapted.library.ad.Ad

actual class AdViewHandler(private val context: Context) {
    actual fun handleLink(ad: Ad) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse(ad.actionPath)
        context.startActivity(intent)
    }

    actual fun handlePopup(ad: Ad) {
        val intent = AndroidWebViewPopupActivity().createActivity(context, ad)
        context.startActivity(intent)
    }
}
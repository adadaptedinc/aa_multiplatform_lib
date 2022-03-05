package com.adadapted.library

import android.app.Activity
import android.content.Intent
import com.adadapted.library.ad.Ad

class AndroidWebViewPopupActivity: Activity() {

    fun createActivity(ad: Ad) {
        val intent = Intent(applicationContext, AndroidWebViewPopupActivity::class.java)
        //intent.putExtra(EXTRA_POPUP_AD, ad) //todo wrap as serializable
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }

    companion object {
        private val EXTRA_POPUP_AD = AndroidWebViewPopupActivity::class.java.name + ".EXTRA_POPUP_AD"
    }
}
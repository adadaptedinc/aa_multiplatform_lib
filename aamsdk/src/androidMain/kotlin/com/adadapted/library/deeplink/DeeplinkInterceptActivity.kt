package com.adadapted.library.deeplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adadapted.library.atl.AddItContentPublisher
import com.adadapted.library.constants.Config.LOG_TAG
import com.adadapted.library.constants.EventStrings
import com.adadapted.library.event.EventClient
import com.adadapted.library.payload.PayloadClient

class DeeplinkInterceptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(LOG_TAG + "Deeplink Intercept Activity Launched.")
        PayloadClient.getInstance().deeplinkInProgress()
        EventClient.getInstance().trackSdkEvent(EventStrings.ADDIT_APP_OPENED)

        try {
            val content = DeeplinkContentParser().parse(intent.data)
            println(LOG_TAG + "AddIt content dispatched to App.")
            AddItContentPublisher.getInstance().publishAddItContent(content)
        } catch (ex: Exception) {
            println(LOG_TAG + "Problem dealing with AddIt content. Recovering. " + ex.message)
            val errorParams: MutableMap<String, String> = HashMap()
            ex.message?.let { errorParams.put(EventStrings.EXCEPTION_MESSAGE, it) }
            EventClient.getInstance().trackSdkError(
                EventStrings.ADDIT_DEEPLINK_HANDLING_ERROR,
                "Problem handling deeplink",
                errorParams
            )

        } finally {
            startActivity(packageManager.getLaunchIntentForPackage(packageName))
        }
        PayloadClient.getInstance().deeplinkCompleted()
        finish()
    }
}
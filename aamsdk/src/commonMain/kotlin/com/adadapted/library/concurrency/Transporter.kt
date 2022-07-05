package com.adadapted.library.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Transporter: TransporterCoroutineScope {
    override fun dispatchToMain(mainFunc: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.Main) {
            mainFunc()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
}

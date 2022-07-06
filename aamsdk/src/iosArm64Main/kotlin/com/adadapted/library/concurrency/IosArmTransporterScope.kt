package com.adadapted.library.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

actual interface TransporterCoroutineScope : CoroutineScope {
    actual fun dispatchToThread(func: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.Main) {
            func()
        }
    }

    actual fun dispatchToMain(func: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.Main) {
            func()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}

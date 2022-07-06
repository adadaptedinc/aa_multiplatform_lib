package com.adadapted.library.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

actual interface TransporterCoroutineScope: CoroutineScope {
    actual fun dispatchToThread(func: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.Default) {
            func()
        }
    }

    actual fun dispatchToMain(func: suspend CoroutineScope.() -> Unit): Job {
        return launch(Dispatchers.Main) {
            func()
        }
    }
}

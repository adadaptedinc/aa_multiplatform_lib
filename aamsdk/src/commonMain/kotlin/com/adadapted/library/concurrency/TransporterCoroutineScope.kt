package com.adadapted.library.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

expect interface TransporterCoroutineScope : CoroutineScope {
    open fun dispatchToThread(func: suspend CoroutineScope.() -> Unit): Job
    open fun dispatchToMain(func: suspend CoroutineScope.() -> Unit): Job
}

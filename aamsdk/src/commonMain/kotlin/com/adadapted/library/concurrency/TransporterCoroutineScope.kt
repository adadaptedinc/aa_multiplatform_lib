package com.adadapted.library.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface TransporterCoroutineScope : CoroutineScope {
    fun dispatchToMain(mainFunc: suspend CoroutineScope.() -> Unit): Job
}
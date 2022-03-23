package com.adadapted.library.network

import io.ktor.client.engine.*
import io.ktor.client.engine.ios.*

actual val defaultPlatformEngine: HttpClientEngine = Ios.create()
package com.adadapted.library.network

import io.ktor.client.engine.*
import io.ktor.client.engine.android.*

actual val defaultPlatformEngine: HttpClientEngine = Android.create()
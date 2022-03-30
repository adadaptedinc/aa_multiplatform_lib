package com.adadapted.library.view

import kotlinx.cinterop.ObjCAction

class Target(private val block: () -> Unit) {
    @ObjCAction
    fun invokeBlock() {
        block()
    }
}
package com.adadapted.library

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}! FROM LIB!"
    }
}
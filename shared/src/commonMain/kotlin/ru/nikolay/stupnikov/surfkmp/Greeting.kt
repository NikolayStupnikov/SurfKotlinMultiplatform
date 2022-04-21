package ru.nikolay.stupnikov.surfkmp

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}
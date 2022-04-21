package ru.nikolay.stupnikov.interactor.api

import io.ktor.client.*
import io.ktor.client.engine.ios.*

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Ios) {
    config(this)
}
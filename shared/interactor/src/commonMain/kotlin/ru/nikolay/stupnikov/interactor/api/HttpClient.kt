package ru.nikolay.stupnikov.interactor.api

import io.ktor.client.*

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient
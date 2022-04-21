package ru.nikolay.stupnikov.interactor.api

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okhttp3.logging.HttpLoggingInterceptor

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)
    engine {
        config {
            retryOnConnectionFailure(false)

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(logging)
        }
    }
}
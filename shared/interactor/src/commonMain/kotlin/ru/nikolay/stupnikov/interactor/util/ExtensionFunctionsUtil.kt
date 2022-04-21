package ru.nikolay.stupnikov.interactor.util

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

fun List<String>.toSingleString(): String {
    val builder = StringBuilder()
    for (value in this) {
        builder.append("$value,")
    }
    return builder.toString().substring(0, builder.toString().length - 1)
}

suspend inline fun <reified T> HttpClient.getWithHeader(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T = get {
    url.takeFrom(urlString)
    headers {
        append(HttpHeaders.ContentType, "application/vnd.api+json")
        append(HttpHeaders.Accept, "application/vnd.api+json")
    }
    block()
}
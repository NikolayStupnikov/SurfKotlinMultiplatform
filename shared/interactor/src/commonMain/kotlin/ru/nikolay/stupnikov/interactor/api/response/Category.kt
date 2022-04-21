package ru.nikolay.stupnikov.interactor.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryApi(
    @SerialName("attributes") val attributes: CategoryAttribute? = null,
    @SerialName("id") val id: Int
)

@Serializable
data class CategoryAttribute(
    @SerialName("title") val title: String? = null,
    @SerialName("slug") val slug: String?
)

@Serializable
data class CategoryResponse(
    @SerialName("data") var result: List<CategoryApi>? = null,
    @SerialName("meta") var meta: Meta?
)
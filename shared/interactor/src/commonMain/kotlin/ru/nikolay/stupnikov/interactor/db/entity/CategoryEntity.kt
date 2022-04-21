package ru.nikolay.stupnikov.interactor.db.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryEntity(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String?,
    @SerialName("slug") val slug: String?,
)
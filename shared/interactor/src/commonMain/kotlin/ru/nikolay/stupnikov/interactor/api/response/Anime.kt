package ru.nikolay.stupnikov.interactor.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nikolay.stupnikov.domain.common.Serializable as CustomSerializable

@Serializable
data class AnimeApi(
    @SerialName("attributes") val attributes: AnimeAttribute? = null,
    @SerialName("id") val id: Int
)

@Serializable
data class AnimeAttribute(
    @SerialName("titles") val titles: Titles? = null,
    @SerialName("posterImage") val posterImage: Poster? = null
)

@Serializable
data class AnimeResponse(
    @SerialName("data") var result: List<AnimeApi>? = null,
    @SerialName("meta") var meta: Meta?
)

@Serializable
data class Meta(@SerialName("count") val count: Int)

@Serializable
data class Poster(@SerialName("original") val original: String? = null)

@Serializable
data class Titles(
    @SerialName("en") val en: String? = null,
    @SerialName("en_jp") val enJp: String? = null,
    @SerialName("ja_jp") val jp: String? = null
): CustomSerializable
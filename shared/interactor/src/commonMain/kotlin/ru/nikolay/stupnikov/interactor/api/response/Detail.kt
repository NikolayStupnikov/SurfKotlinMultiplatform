package ru.nikolay.stupnikov.interactor.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailApi(
    @SerialName("attributes") val attributes: DetailAttribute? = null,
)

@Serializable
data class DetailAttribute(
    @SerialName("description") val description: String? = null,
    @SerialName("ratingRank") val ratingRank: Int? = null,
    @SerialName("startDate") val startDate: String? = null,
    @SerialName("endDate") val endDate: String? = null,
    @SerialName("ageRating") val ageRating: String? = null,
    @SerialName("ageRatingGuide") val ageRatingGuide: String? = null,
    @SerialName("episodeCount") val episodeCount: Int? = null,
    @SerialName("episodeLength") val episodeLength: Int? = null,
    @SerialName("posterImage") val posterImage: Poster? = null
)

@Serializable
data class DetailResponse(
    @SerialName("data") var result: DetailApi? = null
)
package ru.nikolay.stupnikov.domain

import ru.nikolay.stupnikov.domain.common.Serializable

data class Filter(
    val seasons: List<String>,
    val year: String,
    val category: String?,
    val ageRatingList: List<String>
): Serializable
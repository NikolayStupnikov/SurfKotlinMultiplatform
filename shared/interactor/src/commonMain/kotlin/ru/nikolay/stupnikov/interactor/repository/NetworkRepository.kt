package ru.nikolay.stupnikov.interactor.repository

import ru.nikolay.stupnikov.domain.Filter
import ru.nikolay.stupnikov.interactor.api.response.AnimeResponse
import ru.nikolay.stupnikov.interactor.api.response.CategoryResponse
import ru.nikolay.stupnikov.interactor.api.response.DetailResponse

interface NetworkRepository {

    suspend fun requestAnimeList(offset: Int, search: String, filter: Filter?): AnimeResponse
    suspend fun requestCategoryList(offset: Int): CategoryResponse
    suspend fun getDetails(id: Int): DetailResponse
    suspend fun getCategoriesForAnime(id: Int): CategoryResponse
}
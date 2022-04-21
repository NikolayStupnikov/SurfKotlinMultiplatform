package ru.nikolay.stupnikov.interactor.api

import io.ktor.client.HttpClient
import io.ktor.client.request.*
import ru.nikolay.stupnikov.interactor.Setting.PAGE_LIMIT
import ru.nikolay.stupnikov.interactor.api.response.AnimeResponse
import ru.nikolay.stupnikov.interactor.api.response.CategoryResponse
import ru.nikolay.stupnikov.interactor.api.response.DetailResponse
import ru.nikolay.stupnikov.interactor.util.getWithHeader

class BackendApi(
    private val endPoint: String,
    private val httpClient: HttpClient
) {

    suspend fun requestAnimeList(params: Map<String, String>): AnimeResponse {
        return httpClient.getWithHeader("$endPoint/anime") {
            for ((key, value) in params) {
                parameter(key, value)
            }
        }
    }

    suspend fun requestCategoryList(offset: Int): CategoryResponse {
        return httpClient.getWithHeader("$endPoint/categories") {
            parameter("page[limit]", PAGE_LIMIT.toString())
            parameter("page[offset]", offset.toString())
        }
    }

    suspend fun getDetails(id: Int): DetailResponse {
        return httpClient.getWithHeader("$endPoint/anime/$id")
    }

    suspend fun getCategoriesForAnime(id: Int): CategoryResponse {
        return httpClient.getWithHeader("$endPoint/anime/$id/categories") {
            parameter("page[limit]", PAGE_LIMIT.toString())
        }
    }
}


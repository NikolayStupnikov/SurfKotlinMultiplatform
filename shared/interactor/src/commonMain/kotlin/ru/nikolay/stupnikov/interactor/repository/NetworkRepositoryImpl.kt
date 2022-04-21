package ru.nikolay.stupnikov.interactor.repository

import ru.nikolay.stupnikov.domain.Filter
import ru.nikolay.stupnikov.interactor.Setting.PAGE_LIMIT
import ru.nikolay.stupnikov.interactor.Setting.ageRatingList
import ru.nikolay.stupnikov.interactor.Setting.seasons
import ru.nikolay.stupnikov.interactor.api.BackendApi
import ru.nikolay.stupnikov.interactor.api.response.AnimeResponse
import ru.nikolay.stupnikov.interactor.api.response.CategoryResponse
import ru.nikolay.stupnikov.interactor.api.response.DetailResponse
import ru.nikolay.stupnikov.interactor.util.toSingleString

class NetworkRepositoryImpl(
    private val api: BackendApi
) : NetworkRepository {

    override suspend fun requestAnimeList(
        offset: Int,
        search: String,
        filter: Filter?
    ): AnimeResponse {
        val params = HashMap<String, String>()
        params["page[limit]"] = PAGE_LIMIT.toString()
        params["page[offset]"] = offset.toString()
        if (search.isNotEmpty()) {
            params["filter[text]"] = search
        }
        if (filter != null) {
            if (filter.seasons.isNotEmpty() && filter.seasons.size != seasons.size) {
                params["filter[season]"] = filter.seasons.toSingleString()
            }
            if (filter.ageRatingList.isNotEmpty() && filter.ageRatingList.size != ageRatingList.size) {
                params["filter[ageRating]"] = filter.ageRatingList.toSingleString()
            }
            if (filter.year.isNotEmpty()) {
                params["filter[seasonYear]"] = filter.year
            }
            if (!filter.category.isNullOrEmpty()) {
                params["filter[categories]"] = filter.category!!
            }
        }
        return api.requestAnimeList(params)
    }

    override suspend fun requestCategoryList(offset: Int): CategoryResponse {
        return api.requestCategoryList(offset)
    }

    override suspend fun getDetails(id: Int): DetailResponse {
        return api.getDetails(id)
    }

    override suspend fun getCategoriesForAnime(id: Int): CategoryResponse {
        return api.getCategoriesForAnime(id)
    }
}
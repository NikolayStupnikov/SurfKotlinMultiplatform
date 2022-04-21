package ru.nikolay.stupnikov.interactor.interactor

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import ru.nikolay.stupnikov.domain.Filter
import ru.nikolay.stupnikov.interactor.Setting.PAGE_LIMIT
import ru.nikolay.stupnikov.interactor.api.response.AnimeResponse
import ru.nikolay.stupnikov.interactor.api.response.CategoryApi
import ru.nikolay.stupnikov.interactor.api.response.CategoryResponse
import ru.nikolay.stupnikov.interactor.api.response.DetailResponse
import ru.nikolay.stupnikov.interactor.db.entity.CategoryEntity
import ru.nikolay.stupnikov.interactor.repository.DatabaseRepository
import ru.nikolay.stupnikov.interactor.repository.NetworkRepository

class DefaultInteractor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
) {

    suspend fun requestAnimeList(offset: Int, search: String, filter: Filter?): AnimeResponse {
        return networkRepository.requestAnimeList(offset, search, filter)
    }

    fun getDetailsAnime(id: Int): Flow<Pair<DetailResponse, CategoryResponse>> {
        val flowDetail: Flow<DetailResponse> = flow { emit(networkRepository.getDetails(id)) }
        val flowCategory: Flow<CategoryResponse> = flow { emit(networkRepository.getCategoriesForAnime(id)) }
        return flowDetail.zip(flowCategory) { detail, category ->
            Pair(detail, category)
        }
    }

    @FlowPreview
    fun getCategoryList(): Flow<List<CategoryEntity>> {
        return flow {
            emit(databaseRepository.getCountCategories())
        }.flatMapConcat { count ->
            if (count > 0) {
                flow {
                    emit(databaseRepository.getAllCategories())
                }
            } else {
                flow {
                    emit(loadNetworkCategory())
                }
            }
        }
    }

    private suspend fun loadNetworkCategory(): List<CategoryEntity> {
        var offset = 0
        var maxCount = 0
        val categoryList = mutableListOf<CategoryApi>()
        var isAllLoad = false

        suspend fun parseResponse(response: CategoryResponse) {
            if (response.meta != null) {
                maxCount = response.meta!!.count
            }
            if (!response.result.isNullOrEmpty()) {
                offset += PAGE_LIMIT

                if (offset > maxCount) {
                    databaseRepository.insertCategories(
                        mappingCategoryApiInEntity(categoryList)
                    )
                    isAllLoad = true
                } else {
                    categoryList.addAll(response.result!!)
                }
            } else {
                isAllLoad = true
            }
        }

        while (!isAllLoad) {
            parseResponse(networkRepository.requestCategoryList(offset))
        }
        return mappingCategoryApiInEntity(categoryList)
    }

    private fun mappingCategoryApiInEntity(list: List<CategoryApi>): List<CategoryEntity> {
        return  list.map { category ->
            CategoryEntity(
                category.id,
                category.attributes?.title,
                category.attributes?.slug
            )
        }.toList()
    }
}
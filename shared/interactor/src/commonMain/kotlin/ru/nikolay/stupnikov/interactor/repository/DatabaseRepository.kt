package ru.nikolay.stupnikov.interactor.repository

import ru.nikolay.stupnikov.interactor.db.entity.CategoryEntity

interface DatabaseRepository {

    suspend fun getAllCategories(): List<CategoryEntity>
    suspend fun insertCategories(categories: List<CategoryEntity>)
    suspend fun getCountCategories(): Int
}
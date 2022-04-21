package ru.nikolay.stupnikov.interactor.repository

import ru.nikolay.stupnikov.interactor.db.Database
import ru.nikolay.stupnikov.interactor.db.entity.CategoryEntity

class DatabaseRepositoryImpl(
    private val dataBase: Database
): DatabaseRepository {

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return dataBase.getAllCategories()
    }

    override suspend fun insertCategories(categories: List<CategoryEntity>) {
        dataBase.createCategories(categories)
    }

    override suspend fun getCountCategories(): Int {
        return dataBase.getCountCategories()
    }
}
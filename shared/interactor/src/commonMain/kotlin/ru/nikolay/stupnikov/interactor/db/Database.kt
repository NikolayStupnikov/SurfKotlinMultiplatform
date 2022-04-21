package ru.nikolay.stupnikov.interactor.db

import ru.nikolay.stupnikov.interactor.db.entity.CategoryEntity

class Database(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun getAllCategories(): List<CategoryEntity> {
        return dbQuery.getAll(::mapCategory).executeAsList()
    }

    private fun mapCategory(
        id: Long,
        title: String?,
        slug: String?
    ): CategoryEntity {
        return CategoryEntity(id.toInt(), title, slug)
    }

    fun getCountCategories(): Int {
        return dbQuery.getCount().executeAsOne().toInt()
    }

    fun createCategories(categories: List<CategoryEntity>) {
        dbQuery.transaction {
            categories.forEach { categoryEntity ->
                val category = dbQuery.selectCategoryById(categoryEntity.id.toLong()).executeAsOneOrNull()
                if (category == null) {
                    dbQuery.insertCategory(categoryEntity.id.toLong(), categoryEntity.title, categoryEntity.slug)
                }
            }
        }
    }
}
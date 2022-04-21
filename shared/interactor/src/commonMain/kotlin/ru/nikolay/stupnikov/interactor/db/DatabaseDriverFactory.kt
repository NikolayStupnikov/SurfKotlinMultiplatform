package ru.nikolay.stupnikov.interactor.db

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
package ru.nikolay.stupnikov.interactor.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.nikolay.stupnikov.interactor.db.DatabaseDriverFactory

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
}
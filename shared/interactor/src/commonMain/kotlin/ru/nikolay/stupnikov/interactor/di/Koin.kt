package ru.nikolay.stupnikov.interactor.di

import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ru.nikolay.stupnikov.interactor.api.BackendApi
import ru.nikolay.stupnikov.interactor.api.httpClient
import ru.nikolay.stupnikov.interactor.db.Database
import ru.nikolay.stupnikov.interactor.interactor.DefaultInteractor
import ru.nikolay.stupnikov.interactor.repository.DatabaseRepository
import ru.nikolay.stupnikov.interactor.repository.DatabaseRepositoryImpl
import ru.nikolay.stupnikov.interactor.repository.NetworkRepository
import ru.nikolay.stupnikov.interactor.repository.NetworkRepositoryImpl

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            repositoryModule,
            platformModule()
        )
    }

// IOS
fun initKoin() = initKoin {}

val repositoryModule = module {
    single<DatabaseRepository> { DatabaseRepositoryImpl(get()) }
    single<NetworkRepository> { NetworkRepositoryImpl(get()) }

    single {
        httpClient {
            install(JsonFeature) {
                val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                serializer = KotlinxSerializer(json)
            }
        }
    }

    single { "https://kitsu.io/api/edge" }

    single { DefaultInteractor(get(), get()) }
    single { BackendApi(get(), get()) }

    single { Database(get()) }
}
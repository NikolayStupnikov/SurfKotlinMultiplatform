import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    val mokoVersion = "0.12.0"
    val koinVersion = "3.1.2"
    val kloggerVersion = "2.2.0"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:interactor"))
                implementation("dev.icerock.moko:mvvm-core:$mokoVersion")
                implementation("dev.icerock.moko:mvvm-livedata:$mokoVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("com.soywiz.korlibs.klogger:klogger:$kloggerVersion")
            }
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}
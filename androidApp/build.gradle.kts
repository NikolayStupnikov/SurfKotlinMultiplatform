plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "ru.nikolay.stupnikov.surfkmp.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(project(":shared:feature"))
    implementation(project(":shared:interactor"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.core:core-ktx:1.6.0")

    // di
    implementation("io.insert-koin:koin-android:3.1.2")

    // picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    // livedata
    implementation("dev.icerock.moko:mvvm-core:0.12.0")
    implementation("dev.icerock.moko:mvvm-livedata:0.12.0")
    implementation("dev.icerock.moko:mvvm-databinding:0.12.0")
    configurations {
        all {
            exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
        }
    }
}
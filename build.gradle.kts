buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    val kotlinVersion = "1.5.30"
    val sqlDelightVersion: String by project

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.3.0-alpha04")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
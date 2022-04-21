pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "SurfKmp"
include(":androidApp")
include(":shared")
include(":shared:interactor")
include(":shared:domain")
include(":shared:feature")

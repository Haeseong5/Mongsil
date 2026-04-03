pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.google.com")
    }
}

rootProject.name = "Mongsil"

// KMP modules
include(":composeApp")
include(":iosApp")

// Legacy Android app (will be deprecated)
include(":app")

// Core modules (to be migrated to KMP later)
include(":core:network")
include(":core:database")
include(":core:common")
include(":core:repository")

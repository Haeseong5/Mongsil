// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    apply(from = "authentication.gradle")
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.google.services)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.firebase.crashlytics.gradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
        maven {
            setUrl("https://maven.google.com")
        }
    }
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
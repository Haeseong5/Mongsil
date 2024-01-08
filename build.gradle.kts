// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    apply(from = "authentication.gradle")
    repositories {
        jcenter()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath("com.google.gms:google-services:4.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hilt_version")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")

    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
        maven {
            setUrl("https://maven.google.com")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
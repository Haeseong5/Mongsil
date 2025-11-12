plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.cashproject.mongsil.network"
    compileSdk = sdkCompileVersion

    defaultConfig {
        minSdk = sdkMinVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.android.material)

    //coil
    implementation(libs.coil.compose)

    //network & json
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava2)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)


    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.config.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

}